package org.gabrielbarrilli.securitytelegram.web.dto.mapper;

import org.gabrielbarrilli.securitytelegram.model.Usuario;
import org.gabrielbarrilli.securitytelegram.web.dto.UsuarioCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {
    
    public static Usuario toUsuario(UsuarioCreateDto dto) {
        return new ModelMapper().map(dto, Usuario.class);
    }

    public static UsuarioResponseDto toDto(Usuario usuario) {
        String role = usuario.getRole().name().substring("ROLE_".length());
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };

        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    public static List<UsuarioResponseDto> toListDto(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::toDto).collect(Collectors.toList());
    }
}
