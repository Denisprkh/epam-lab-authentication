package com.epam.esm.service.util.linkbuilder.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.util.linkbuilder.LinkName;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ResponseOrderDtoLinkBuilderImpl implements NavigationLinkBuilder<ResponseOrderDto> {


    @Override
    public ResponseOrderDto buildLinks(ResponseOrderDto responseOrderDto) {
        responseOrderDto.add(linkTo(OrderController.class).slash(responseOrderDto.getId()).withSelfRel());
        responseOrderDto.add(linkTo(methodOn(OrderController.class).findAllOrders(new Pagination(), new HashMap<>()))
                .withRel(LinkName.ALL_RESOURCES));
        buildLinksToGiftCertificateById(responseOrderDto);
        responseOrderDto.add(linkTo(methodOn(UserController.class).findUserById(responseOrderDto.getUserId())).
                withRel(LinkName.USER));

        return responseOrderDto;
    }

    private void buildLinksToGiftCertificateById(ResponseOrderDto responseOrderDto) {
        List<Integer> certificatesId = responseOrderDto.getCertificatesId();
        List<Link> links = certificatesId.stream().map(certId -> linkTo(methodOn(GiftCertificateController.class).findGiftCertificateById(certId)).withRel(LinkName.GIFT_CERTIFICATE))
                .collect(Collectors.toList());
        responseOrderDto.add(links);
    }

}
