package org.gabrielbarrilli.securitytelegram.web.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.gabrielbarrilli.securitytelegram.model.Vaga;
import org.gabrielbarrilli.securitytelegram.web.dto.VagaCreateDto;
import org.gabrielbarrilli.securitytelegram.web.dto.VagaResponseDto;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga(VagaCreateDto dto) {

        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDto toDto(Vaga vaga) {

        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
