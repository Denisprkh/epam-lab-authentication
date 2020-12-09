package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.mapper.util.LocalDateTimeConverter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ResponseOrderDtoMapper implements DtoMapper<ResponseOrderDto, Order> {

    private final LocalDateTimeConverter localDateTimeConverter;

    public ResponseOrderDtoMapper(LocalDateTimeConverter localDateTimeConverter) {
        this.localDateTimeConverter = localDateTimeConverter;
    }

    @Override
    public ResponseOrderDto toDto(Order order) {
        ResponseOrderDto responseOrderDto = new ResponseOrderDto();
        responseOrderDto.setId(order.getId());
        responseOrderDto.setCost(order.getCost());
        responseOrderDto.setPurchaseDate(localDateTimeConverter.convertLocalDateTimeToISOFormat
                (order.getPurchaseDate()));
        responseOrderDto.setUserId(order.getUser().getId());
        responseOrderDto.setCertificatesId(order.getGiftCertificates().stream().map(GiftCertificate::getId)
                .collect(Collectors.toList()));
        return responseOrderDto;
    }

    @Override
    public Order toModel(ResponseOrderDto responseOrderDto) {
        Order order = new Order();
        order.setPurchaseDate(localDateTimeConverter.convertStringToLocalDateTime(responseOrderDto.getPurchaseDate()));
        order.setCost(responseOrderDto.getCost());
        order.setUser(new User(responseOrderDto.getUserId()));
        order.setGiftCertificates(responseOrderDto.getCertificatesId().stream().map(GiftCertificate::new)
                .collect(Collectors.toList()));
        order.setId(responseOrderDto.getId());
        return order;
    }


}
