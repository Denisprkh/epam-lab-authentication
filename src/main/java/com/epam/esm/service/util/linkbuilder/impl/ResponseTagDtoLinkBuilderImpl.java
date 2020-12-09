package com.epam.esm.service.util.linkbuilder.impl;

import com.epam.esm.controller.TagController;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.util.linkbuilder.LinkName;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import com.epam.esm.dto.ResponseTagDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResponseTagDtoLinkBuilderImpl implements NavigationLinkBuilder<ResponseTagDto> {

    @Override
    public ResponseTagDto buildLinks(ResponseTagDto responseTagDto) {
        responseTagDto.add(linkTo(TagController.class).slash(responseTagDto.getId()).withSelfRel());
        responseTagDto.add(linkTo(methodOn(TagController.class).findAllTags(new Pagination(), new HashMap<>()))
                .withRel(LinkName.ALL_RESOURCES));
        return responseTagDto;
    }

}
