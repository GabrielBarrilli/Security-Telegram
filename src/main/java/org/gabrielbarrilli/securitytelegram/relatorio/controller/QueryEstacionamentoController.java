package org.gabrielbarrilli.securitytelegram.relatorio.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.gabrielbarrilli.securitytelegram.relatorio.service.RelatorioEstacionamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorio")
public class QueryEstacionamentoController {

    private final RelatorioEstacionamentoService relatorioEstacionamentoService;

    public QueryEstacionamentoController(RelatorioEstacionamentoService relatorioEstacionamentoService) {
        this.relatorioEstacionamentoService = relatorioEstacionamentoService;
    }

    @GetMapping("/{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public void estacionamentoRelatorio(@PathVariable String cpf, HttpServletResponse response) throws Exception {
        relatorioEstacionamentoService.estacionamentoRelatorio(cpf, response);
    }
}
