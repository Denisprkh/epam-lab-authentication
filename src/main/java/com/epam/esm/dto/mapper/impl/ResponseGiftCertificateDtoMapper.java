package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.ResponseGiftCertificateDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.dto.mapper.util.LocalDateTimeConverter;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.stream.Collectors;


@Component
public class ResponseGiftCertificateDtoMapper implements DtoMapper<ResponseGiftCertificateDto, GiftCertificate> {

    private final ResponseTagDtoMapper responseTagDtoMapper;
    private final LocalDateTimeConverter localDateTimeConverter;

    public ResponseGiftCertificateDtoMapper(ResponseTagDtoMapper responseTagDtoMapper, LocalDateTimeConverter localDateTimeConverter) {
        this.responseTagDtoMapper = responseTagDtoMapper;
        this.localDateTimeConverter = localDateTimeConverter;
    }

    @Override
    public ResponseGiftCertificateDto toDto(GiftCertificate giftCertificate) {
        ResponseGiftCertificateDto responseGiftCertificateDto = new ResponseGiftCertificateDto();
        responseGiftCertificateDto.setId(giftCertificate.getId());
        responseGiftCertificateDto.setName(giftCertificate.getName());
        responseGiftCertificateDto.setCreateDate(localDateTimeConverter.convertLocalDateTimeToISOFormat
                (giftCertificate.getCreateDate()));
        responseGiftCertificateDto.setDescription(giftCertificate.getDescription());
        responseGiftCertificateDto.setLastUpdateDate(localDateTimeConverter.convertLocalDateTimeToISOFormat
                (giftCertificate.getLastUpdateDate()));
        responseGiftCertificateDto.setPrice(giftCertificate.getPrice());
        responseGiftCertificateDto.setTags(giftCertificate.getTags().stream().map(responseTagDtoMapper::toDto)
                .collect(Collectors.toList()));
        responseGiftCertificateDto.setDuration(giftCertificate.getDuration().toString());
        return responseGiftCertificateDto;
    }

    @Override
    public GiftCertificate toModel(ResponseGiftCertificateDto responseGiftCertificateDto) {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(responseGiftCertificateDto.getId());
        giftCertificate.setLastUpdateDate(localDateTimeConverter.convertStringToLocalDateTime
                (responseGiftCertificateDto.getLastUpdateDate()));
        giftCertificate.setDuration(Duration.parse(responseGiftCertificateDto.getDuration()));
        giftCertificate.setPrice(responseGiftCertificateDto.getPrice());
        giftCertificate.setName(responseGiftCertificateDto.getName());
        giftCertificate.setDescription(responseGiftCertificateDto.getDescription());
        giftCertificate.setCreateDate(localDateTimeConverter.convertStringToLocalDateTime
                (responseGiftCertificateDto.getCreateDate()));
        giftCertificate.setTags(responseGiftCertificateDto.getTags().stream().map(responseTagDtoMapper::toModel)
                .collect(Collectors.toList()));
        return giftCertificate;
    }




}
