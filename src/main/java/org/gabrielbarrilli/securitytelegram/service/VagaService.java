package org.gabrielbarrilli.securitytelegram.service;

import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.repository.VagaRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VagaService {

    private final VagaRepository vagaRepository;
}
