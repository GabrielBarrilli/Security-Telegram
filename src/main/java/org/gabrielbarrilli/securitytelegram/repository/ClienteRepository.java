package org.gabrielbarrilli.securitytelegram.repository;

import org.gabrielbarrilli.securitytelegram.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
