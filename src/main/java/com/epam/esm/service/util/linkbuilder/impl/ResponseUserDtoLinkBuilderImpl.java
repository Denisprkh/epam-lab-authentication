package com.epam.esm.service.util.linkbuilder.impl;

import com.epam.esm.controller.UserController;
import com.epam.esm.controller.UserPurchaseController;
import com.epam.esm.dto.ResponseUserDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.util.linkbuilder.LinkName;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResponseUserDtoLinkBuilderImpl implements NavigationLinkBuilder<ResponseUserDto> {

    @Override
    public ResponseUserDto buildLinks(ResponseUserDto responseUserDto) {
        responseUserDto.add(linkTo(UserController.class).slash(responseUserDto.getId()).withSelfRel());
        responseUserDto.add(linkTo(methodOn(UserPurchaseController.class).findUsersPurchases(responseUserDto.getId(), new HashSet<>()))
                .withRel(LinkName.USER_PURCHASES));
        responseUserDto.add(linkTo(methodOn(UserController.class).findAllUsers(new Pagination(), new HashMap<>()))
                .withRel(LinkName.ALL_RESOURCES));
        return responseUserDto;
    }
}
