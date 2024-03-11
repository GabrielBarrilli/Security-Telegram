package org.gabrielbarrilli.securitytelegram.web.controller;

import lombok.RequiredArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.Usuario;
import org.gabrielbarrilli.securitytelegram.model.dto.UsuarioCreateDto;
import org.gabrielbarrilli.securitytelegram.model.dto.UsuarioResponseDto;
import org.gabrielbarrilli.securitytelegram.model.dto.UsuarioSenhaDto;
import org.gabrielbarrilli.securitytelegram.model.dto.mapper.UsuarioMapper;
import org.gabrielbarrilli.securitytelegram.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/create")
    public ResponseEntity<UsuarioResponseDto> create(@RequestBody UsuarioCreateDto dto) {

        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(dto));

        return ResponseEntity.status(CREATED)
                .body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {

        Usuario user = usuarioService.buscarPorId(id);

        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Usuario>> getAll() {

        List<Usuario> users = usuarioService.getAll();

        return ResponseEntity.ok(users);
    }

    @PatchMapping("/updatePassword/{id}")
    public ResponseEntity<Usuario> updatePassword(@PathVariable Long id, @RequestBody UsuarioSenhaDto dto) {

        Usuario user = usuarioService.updatePassword(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());

        return ResponseEntity.ok(user);
    }
}
