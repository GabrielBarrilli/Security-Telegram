package org.gabrielbarrilli.securitytelegram.web.dto.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.gabrielbarrilli.securitytelegram.web.dto.PageableDto;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public static PageableDto toDto(Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}
