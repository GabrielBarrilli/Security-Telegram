package org.gabrielbarrilli.securitytelegram.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.jwt.JwtUserDetails;
import org.gabrielbarrilli.securitytelegram.model.ClienteVaga;
import org.gabrielbarrilli.securitytelegram.repository.projection.ClienteVagaProjection;
import org.gabrielbarrilli.securitytelegram.service.ClienteService;
import org.gabrielbarrilli.securitytelegram.service.ClienteVagaService;
import org.gabrielbarrilli.securitytelegram.service.EstacionamentoService;
import org.gabrielbarrilli.securitytelegram.service.JasperService;
import org.gabrielbarrilli.securitytelegram.web.dto.EstacionamentoCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.EstacionamentoResponseDto;
import org.gabrielbarrilli.securitytelegram.web.dto.PageableDto;
import org.gabrielbarrilli.securitytelegram.web.dto.mapper.ClienteVagaMapper;
import org.gabrielbarrilli.securitytelegram.web.dto.mapper.PageableMapper;
import org.gabrielbarrilli.securitytelegram.web.exception.ErrorMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final JasperService jasperService;

    @Operation(summary = "Operação de check-in", description = "Recurso para dar entrada de um veículo no estacionamento. " +
            "A requisição exige uso de um bearer token. Acesso restrito a Role = 'ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL de acesso do recurso criado"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "causas possíveis:<br/>" +
                            "- CPF o cliente não cadastrado no sistema; <br/>" +
                            "- Nenhuma vaga livre foi localizada.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Usuário sem permissão para acessar este recurso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkIn(@RequestBody @Valid EstacionamentoCreateDto dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();

        return ResponseEntity.created(location).body(responseDto);
    }

    @Operation(summary = "Localizar um veículo estacionado", description = "Recurso para retornar um veículo estacionado " +
            "pelo nº do recibo. Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = PATH, name = "recibo", description = "Número do rebibo gerado pelo check-in")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Número do recibo não encontrado.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/check-in/{recibo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity<EstacionamentoResponseDto> findByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.findByRecibo(recibo);

        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Operação de check-out", description = "Recurso para dar saída de um veículo do estacionamento. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = {@Parameter(in = PATH, name = "recibo", description = "Número do rebibo gerado pelo check-in",
                    required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso atualzado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Número do recibo inexistente ou " +
                            "o veículo já passou pelo check-out.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/check-out/{recibo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkOut(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);

        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Localizar os registros de estacionamentos do cliente por CPF", description = "Localizar os " +
            "registros de estacionamentos do cliente por CPF. Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = PATH, name = "cpf", description = "Nº do CPF referente ao cliente a ser consultado",
                            required = true
                    ),
                    @Parameter(in = QUERY, name = "page", description = "Representa a página retornada",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))
                    ),
                    @Parameter(in = QUERY, name = "size", description = "Representa o total de elementos por página",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))
                    ),
                    @Parameter(in = QUERY, name = "sort", description = "Campo padrão de ordenação 'dataEntrada,asc'. ",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                            hidden = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAllEstacionamentosByCpf(@PathVariable String cpf, @Parameter(hidden = true)
    @PageableDefault(size = 5, sort = "dataEntrada",
            direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorClienteCpf(cpf, pageable);
        PageableDto dto = PageableMapper.toDto(projection);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Localizar os registros de estacionamentos do cliente logado",
            description = "Localizar os registros de estacionamentos do cliente logado. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                            description = "Campo padrão de ordenação 'dataEntrada,asc'. ")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<PageableDto> getAllEstacionamentosDoCLiente(@AuthenticationPrincipal JwtUserDetails user, @Parameter(hidden = true)
    @PageableDefault(size = 5, sort = "dataEntrada",
            direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorUsuarioId(user.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projection);

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Relatório em PDF com os estacionamentos do cliente",
            description = "Recurso para gerar um relatório com os estacionamentos do cliente. " +
                    "Requisição exige uso de um bearer token.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso",
                            content = @Content(mediaType = "application/pdf",
                                    schema = @Schema(implementation = EstacionamentoResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/relatorio")
    public ResponseEntity<Void> getRelatorio(HttpServletResponse response, @AuthenticationPrincipal JwtUserDetails user) throws IOException {
        String cpf = clienteService.findByUsuarioId(user.getId()).getCpf();
        jasperService.addParams("CPF", cpf);

        byte[] bytes = jasperService.gerarPdf();

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-disposition", "inline; filename=" + System.currentTimeMillis() + ".pdf");
        response.getOutputStream().write(bytes);

        return ResponseEntity.ok().build();
    }
}