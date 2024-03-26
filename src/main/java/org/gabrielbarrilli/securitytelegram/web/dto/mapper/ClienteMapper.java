package org.gabrielbarrilli.securitytelegram.web.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.Cliente;
import org.gabrielbarrilli.securitytelegram.web.dto.ClienteCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.ClienteResponseDto;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }

    public static List<ClienteResponseDto> toListDto(List<Cliente> cliente) {
        return cliente.stream().map(ClienteMapper::toDto).collect(Collectors.toList());
    }
}
