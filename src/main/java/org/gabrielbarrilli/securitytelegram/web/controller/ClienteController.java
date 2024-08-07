package org.gabrielbarrilli.securitytelegram.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.jwt.JwtUserDetails;
import org.gabrielbarrilli.securitytelegram.model.Cliente;
import org.gabrielbarrilli.securitytelegram.repository.projection.ClienteProjection;
import org.gabrielbarrilli.securitytelegram.service.ClienteService;
import org.gabrielbarrilli.securitytelegram.service.UsuarioService;
import org.gabrielbarrilli.securitytelegram.web.dto.ClienteCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.ClienteResponseDto;
import org.gabrielbarrilli.securitytelegram.web.dto.PageableDto;
import org.gabrielbarrilli.securitytelegram.web.dto.mapper.ClienteMapper;
import org.gabrielbarrilli.securitytelegram.web.dto.mapper.PageableMapper;
import org.gabrielbarrilli.securitytelegram.web.exception.ErrorMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Clientes", description = "Contém todas as operações relativas ao recurso de um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente", description = "Recurso para criar novo cliente vinculado a um usuário cadastrado. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role = 'CLIENTE'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente já possui CPF cadastrado",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de ADMIN",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto,
                                                     @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);

        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));

        clienteService.salvar(cliente);

        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Achar um cliente pelo ID", description = "Recurso para achar cliente pelo id. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role = 'ADMIN'. ",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorno de dados do cliente",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> findByid(@PathVariable Long id) {

        Cliente cliente = clienteService.findById(id);

        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Recuperar lista de clientes",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role = 'ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            content = @Content(schema = @Schema(type = "string", defaultValue = "id,asc")),
                            description = "Representa a ordenação dos resultados. Múltiplos critérios de ordenação são suportados"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Retorno de dados dos clientes",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil do CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAll(@Parameter(hidden = true)
                                                   @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {

        Page<ClienteProjection> clientes = clienteService.findAll(pageable);

        return ResponseEntity.ok(PageableMapper.toDto(clientes));
    }

    @Operation(summary = "Recuperar dados de um cliente autenticado", description = "Recurso para recuperar dados do cliente autenticado. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role= 'CLIENTE'. ",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido para ADMIN",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> getDetalhes(@AuthenticationPrincipal JwtUserDetails userDetails) {

        Cliente cliente = clienteService.findByUsuarioId(userDetails.getId());

        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }
}
