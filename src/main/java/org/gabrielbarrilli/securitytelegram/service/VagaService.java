package org.gabrielbarrilli.securitytelegram.service;

import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.exception.CodigoUniqueValidationException;
import org.gabrielbarrilli.securitytelegram.exception.EntityNotFoundException;
import org.gabrielbarrilli.securitytelegram.model.Vaga;
import org.gabrielbarrilli.securitytelegram.repository.VagaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.gabrielbarrilli.securitytelegram.model.Vaga.StatusVaga.LIVRE;

@RequiredArgsConstructor
@Service
public class VagaService {

    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga) {
        try {
            return vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
            throw new CodigoUniqueValidationException(String.format("Vaga com código '%s' já cadastrada", vaga.getCodigo()));
        }
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo) {
        return vagaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Vaga com código '%s' não foi encontrada", codigo)));
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorVagaLivre() {
        return vagaRepository.findFirstByStatus(LIVRE)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma vaga livre foi encontrada"));
    }
}
