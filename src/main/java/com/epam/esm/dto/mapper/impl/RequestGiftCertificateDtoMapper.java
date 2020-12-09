package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.RequestGiftCertificateDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

import java.time.Duration;
import java.util.stream.Collectors;

@Component
public class RequestGiftCertificateDtoMapper implements DtoMapper<RequestGiftCertificateDto, GiftCertificate> {

    private final RequestTagDtoMapper requestTagDtoMapper;

    public RequestGiftCertificateDtoMapper(RequestTagDtoMapper requestTagDtoMapper) {
        this.requestTagDtoMapper = requestTagDtoMapper;
    }

    @Override
    public RequestGiftCertificateDto toDto(GiftCertificate giftCertificate) {
        RequestGiftCertificateDto requestGiftCertificateDto = new RequestGiftCertificateDto();
        requestGiftCertificateDto.setDescription(giftCertificate.getDescription());
        requestGiftCertificateDto.setDuration(giftCertificate.getDuration().toString());
        requestGiftCertificateDto.setName(giftCertificate.getName());
        requestGiftCertificateDto.setPrice(giftCertificate.getPrice());
        requestGiftCertificateDto.setTags(giftCertificate.getTags().stream().map(requestTagDtoMapper::toDto)
                .collect(Collectors.toList()));
        return requestGiftCertificateDto;
    }

    @Override
    public GiftCertificate toModel(RequestGiftCertificateDto requestGiftCertificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName(requestGiftCertificateDto.getName());
        giftCertificate.setDescription(requestGiftCertificateDto.getDescription());
        if (nonNull(requestGiftCertificateDto.getTags())) {
            giftCertificate.setTags(requestGiftCertificateDto.getTags().stream().map(requestTagDtoMapper::toModel).
                    collect(Collectors.toList()));
        }
        giftCertificate.setPrice(requestGiftCertificateDto.getPrice());
        if (nonNull(requestGiftCertificateDto.getDuration())) {
            giftCertificate.setDuration(Duration.parse(requestGiftCertificateDto.getDuration()));
        }
        return giftCertificate;
    }

}
