package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.ResponsePurchaseDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.mapper.util.LocalDateTimeConverter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ResponsePurchaseDtoMapper implements DtoMapper<ResponsePurchaseDto, Purchase> {

    private final LocalDateTimeConverter localDateTimeConverter;

    public ResponsePurchaseDtoMapper(LocalDateTimeConverter localDateTimeConverter) {
        this.localDateTimeConverter = localDateTimeConverter;
    }

    @Override
    public ResponsePurchaseDto toDto(Purchase purchase) {
        ResponsePurchaseDto responsePurchaseDto = new ResponsePurchaseDto();
        responsePurchaseDto.setId(purchase.getId());
        responsePurchaseDto.setCost(purchase.getCost());
        responsePurchaseDto.setPurchaseDate(localDateTimeConverter.convertLocalDateTimeToISOFormat
                (purchase.getPurchaseDate()));
        responsePurchaseDto.setUserId(purchase.getUser().getId());
        responsePurchaseDto.setCertificatesId(purchase.getGiftCertificates().stream().map(GiftCertificate::getId)
                .collect(Collectors.toList()));
        return responsePurchaseDto;
    }

    @Override
    public Purchase toModel(ResponsePurchaseDto responsePurchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setPurchaseDate(localDateTimeConverter.convertStringToLocalDateTime(responsePurchaseDto.getPurchaseDate()));
        purchase.setCost(responsePurchaseDto.getCost());
        purchase.setUser(new User(responsePurchaseDto.getUserId()));
        purchase.setGiftCertificates(responsePurchaseDto.getCertificatesId().stream().map(GiftCertificate::new)
                .collect(Collectors.toList()));
        purchase.setId(responsePurchaseDto.getId());
        return purchase;
    }


}
