package org.gabrielbarrilli.securitytelegram.web.controller;

import jakarta.validation.Valid;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/create")
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto dto) {

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
    public ResponseEntity<List<UsuarioResponseDto>> getAll() {

        List<Usuario> users = usuarioService.getAll();

        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }

    @PatchMapping("/updatePassword/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto) {

        Usuario user = usuarioService.updatePassword(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());

        return ResponseEntity.noContent().build();
    }


}
