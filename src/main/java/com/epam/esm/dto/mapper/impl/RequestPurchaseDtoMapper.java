package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.RequestPurchaseDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RequestPurchaseDtoMapper implements DtoMapper<RequestPurchaseDto, Purchase> {

    @Override
    public RequestPurchaseDto toDto(Purchase purchase) {
        RequestPurchaseDto requestPurchaseDto = new RequestPurchaseDto();
        requestPurchaseDto.setUserId(purchase.getUser().getId());
        requestPurchaseDto.setCertificatesId(purchase.getGiftCertificates().stream().map(GiftCertificate::getId)
                .collect(Collectors.toList()));
        return requestPurchaseDto;
    }

    @Override
    public Purchase toModel(RequestPurchaseDto requestPurchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setUser(new User(requestPurchaseDto.getUserId()));
        purchase.setGiftCertificates(requestPurchaseDto.getCertificatesId().stream().map(GiftCertificate::new).collect(Collectors.toList()));
        return purchase;
    }

}
