package com.epam.esm.service.util.linkbuilder.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.entity.GiftCertificateCriteria;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.util.linkbuilder.LinkName;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import com.epam.esm.dto.ResponseGiftCertificateDto;
import com.epam.esm.dto.ResponseTagDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static java.util.Objects.nonNull;

@Component
public class ResponseGiftCertificateDtoLinkBuilderImpl implements NavigationLinkBuilder<ResponseGiftCertificateDto> {

    private final NavigationLinkBuilder<ResponseTagDto> tagLinkBuilder;

    public ResponseGiftCertificateDtoLinkBuilderImpl(NavigationLinkBuilder<ResponseTagDto> tagLinkBuilder) {
        this.tagLinkBuilder = tagLinkBuilder;
    }

    @Override
    public ResponseGiftCertificateDto buildLinks(ResponseGiftCertificateDto responseGiftCertificateDto) {
        responseGiftCertificateDto.add(linkTo(GiftCertificateController.class).slash(responseGiftCertificateDto.getId()).withSelfRel());
        responseGiftCertificateDto.add(linkTo(methodOn(GiftCertificateController.class).findAllGiftCertificates
                (new GiftCertificateCriteria(), new HashMap<>(), new Pagination())).withRel(LinkName.ALL_RESOURCES));
        if (nonNull(responseGiftCertificateDto.getTags())) {
            responseGiftCertificateDto.getTags().forEach(tagLinkBuilder::buildLinks);
        }
        return responseGiftCertificateDto;
    }
}
