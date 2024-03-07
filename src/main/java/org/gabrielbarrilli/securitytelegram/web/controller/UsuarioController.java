package org.gabrielbarrilli.securitytelegram.web.controller;

import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.Usuario;
import org.gabrielbarrilli.securitytelegram.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/create")
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {

        Usuario user = usuarioService.salvar(usuario);

        return ResponseEntity.status(CREATED)
                .body(user);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {

        Usuario user = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/updatePassword/{id}")
    public ResponseEntity<Usuario> updatePassword(@PathVariable Long id, @RequestBody Usuario usuario) {

        Usuario user = usuarioService.updatePassword(id, usuario.getPassword());

        return ResponseEntity.ok(user);
    }
}
