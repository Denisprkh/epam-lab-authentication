package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.RequestTagDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class RequestTagDtoMapper implements DtoMapper<RequestTagDto, Tag> {

    @Override
    public RequestTagDto toDto(Tag tag) {
        return new RequestTagDto(tag.getName());
    }

    @Override
    public Tag toModel(RequestTagDto requestTagDto) {
        return new Tag(requestTagDto.getName());
    }
}
