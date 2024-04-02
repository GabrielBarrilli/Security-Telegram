package org.gabrielbarrilli.securitytelegram.web.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.ClienteVaga;
import org.gabrielbarrilli.securitytelegram.web.dto.EstacionamentoCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.EstacionamentoResponseDto;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga(EstacionamentoCreateDto dto) {
        return new ModelMapper().map(dto, ClienteVaga.class);
    }

    public static EstacionamentoResponseDto toDto(ClienteVaga clienteVaga) {
        return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
    }
}
