package org.gabrielbarrilli.securitytelegram.service;

import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.ClienteVaga;
import org.gabrielbarrilli.securitytelegram.repository.ClienteVagaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {

    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga) {
        return clienteVagaRepository.save(clienteVaga);
    }
}
