package org.gabrielbarrilli.securitytelegram.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.service.VagaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Vagas", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de vagas.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;
}
