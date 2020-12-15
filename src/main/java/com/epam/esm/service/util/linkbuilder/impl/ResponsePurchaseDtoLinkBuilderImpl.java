package com.epam.esm.service.util.linkbuilder.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.PurchaseController;
import com.epam.esm.controller.UserController;
import com.epam.esm.dto.ResponsePurchaseDto;
import com.epam.esm.controller.util.Pagination;
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
public class ResponsePurchaseDtoLinkBuilderImpl implements NavigationLinkBuilder<ResponsePurchaseDto> {


    @Override
    public ResponsePurchaseDto buildLinks(ResponsePurchaseDto responsePurchaseDto) {
        responsePurchaseDto.add(linkTo(PurchaseController.class).slash(responsePurchaseDto.getId()).withSelfRel());
        responsePurchaseDto.add(linkTo(methodOn(PurchaseController.class).findAllPurchases(new Pagination(), new HashMap<>()))
                .withRel(LinkName.ALL_RESOURCES));
        buildLinksToGiftCertificateById(responsePurchaseDto);
        responsePurchaseDto.add(linkTo(methodOn(UserController.class).findUserById(responsePurchaseDto.getUserId())).
                withRel(LinkName.USER));

        return responsePurchaseDto;
    }

    private void buildLinksToGiftCertificateById(ResponsePurchaseDto responsePurchaseDto) {
        List<Integer> certificatesId = responsePurchaseDto.getCertificatesId();
        List<Link> links = certificatesId.stream().map(certId -> linkTo(methodOn(GiftCertificateController.class).findGiftCertificateById(certId)).withRel(LinkName.GIFT_CERTIFICATE))
                .collect(Collectors.toList());
        responsePurchaseDto.add(links);
    }

}
