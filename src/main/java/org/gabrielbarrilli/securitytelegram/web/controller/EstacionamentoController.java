package org.gabrielbarrilli.securitytelegram.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.ClienteVaga;
import org.gabrielbarrilli.securitytelegram.service.EstacionamentoService;
import org.gabrielbarrilli.securitytelegram.web.dto.EstacionamentoCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.EstacionamentoResponseDto;
import org.gabrielbarrilli.securitytelegram.web.dto.mapper.ClienteVagaMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;

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
}
