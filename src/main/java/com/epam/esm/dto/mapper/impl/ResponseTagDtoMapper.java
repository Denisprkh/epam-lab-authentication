package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.ResponseTagDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

@Component
public class ResponseTagDtoMapper implements DtoMapper<ResponseTagDto, Tag> {

    @Override
    public ResponseTagDto toDto(Tag tag) {
        ResponseTagDto responseTagDto = new ResponseTagDto();
        responseTagDto.setId(tag.getId());
        responseTagDto.setName(tag.getName());
        return responseTagDto;
    }

    @Override
    public Tag toModel(ResponseTagDto responseTagDto) {
        Tag tag = new Tag();
        tag.setId(responseTagDto.getId());
        tag.setName(responseTagDto.getName());
        return tag;
    }
}
