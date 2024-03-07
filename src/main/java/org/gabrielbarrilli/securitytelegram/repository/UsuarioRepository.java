package org.gabrielbarrilli.securitytelegram.repository;

import org.gabrielbarrilli.securitytelegram.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}