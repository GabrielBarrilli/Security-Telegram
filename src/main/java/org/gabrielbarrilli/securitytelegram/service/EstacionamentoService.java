package org.gabrielbarrilli.securitytelegram.service;

import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.Cliente;
import org.gabrielbarrilli.securitytelegram.model.ClienteVaga;
import org.gabrielbarrilli.securitytelegram.model.Vaga;
import org.gabrielbarrilli.securitytelegram.util.EstacionamentoUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class EstacionamentoService {

    private final ClienteVagaService clienteVagaService;

    private final ClienteService clienteService;

    private final VagaService vagaService;

    @Transactional
    public ClienteVaga checkIn(ClienteVaga clienteVaga) {
        Cliente cliente = clienteService.findByCpf(clienteVaga.getCliente().getCpf());

        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.buscarPorVagaLivre();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());

        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibo());

        return clienteVagaService.salvar(clienteVaga);
    }
}
