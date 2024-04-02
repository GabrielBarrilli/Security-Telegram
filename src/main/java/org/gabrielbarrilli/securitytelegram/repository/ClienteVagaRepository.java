package org.gabrielbarrilli.securitytelegram.repository;

import org.gabrielbarrilli.securitytelegram.model.ClienteVaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {
}
