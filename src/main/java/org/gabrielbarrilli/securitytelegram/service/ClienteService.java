package org.gabrielbarrilli.securitytelegram.service;

import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.exception.CpfUniqueViolationException;
import org.gabrielbarrilli.securitytelegram.exception.EntityNotFoundException;
import org.gabrielbarrilli.securitytelegram.model.Cliente;
import org.gabrielbarrilli.securitytelegram.repository.ClienteRepository;
import org.gabrielbarrilli.securitytelegram.repository.projection.ClienteProjection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                    String.format("CPF '%s' já está cadastrado no sistema", cliente.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Cliente findById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário %s não encontrado." , id)));
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> findAll(Pageable pageable) {
        return clienteRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente findByUsuarioId(Long id) {
        return clienteRepository.findByUsuarioId(id);
    }

    @Transactional(readOnly = true)
    public Cliente findByCpf(String cpf) {
        return clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cliente com CPF '%s' não encontrado", cpf)));
    }
}
