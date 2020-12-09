package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.RequestOrderDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RequestOrderDtoMapper implements DtoMapper<RequestOrderDto, Order> {

    @Override
    public RequestOrderDto toDto(Order order) {
        RequestOrderDto requestOrderDto = new RequestOrderDto();
        requestOrderDto.setUserId(order.getUser().getId());
        requestOrderDto.setCertificatesId(order.getGiftCertificates().stream().map(GiftCertificate::getId)
                .collect(Collectors.toList()));
        return requestOrderDto;
    }

    @Override
    public Order toModel(RequestOrderDto requestOrderDto) {
        Order order = new Order();
        order.setUser(new User(requestOrderDto.getUserId()));
        order.setGiftCertificates(requestOrderDto.getCertificatesId().stream().map(GiftCertificate::new).collect(Collectors.toList()));
        return order;
    }

}
