package com.epam.esm.service.impl;

import com.epam.esm.dao.giftcertificate.GiftCertificateDao;
import com.epam.esm.dto.RequestGiftCertificateDto;
import com.epam.esm.dto.ResponseGiftCertificateDto;
import com.epam.esm.dto.mapper.impl.RequestGiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseGiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseTagDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.dao.util.GiftCertificateCriteria;
import com.epam.esm.controller.util.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;


@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final RequestGiftCertificateDtoMapper requestGiftCertificateDtoMapper;
    private final ResponseGiftCertificateDtoMapper responseGiftCertificateDtoMapper;
    private final TagService tagService;
    private final PaginationContextBuilder paginationContextBuilder;
    private final ResponseTagDtoMapper responseTagDtoMapper;

    public GiftCertificateServiceImpl(GiftCertificateDao giftCertificateDao,
                                      RequestGiftCertificateDtoMapper requestGiftCertificateDtoMapper,
                                      ResponseGiftCertificateDtoMapper responseGiftCertificateDtoMapper,
                                      TagService tagService, PaginationContextBuilder paginationContextBuilder,
                                      ResponseTagDtoMapper responseTagDtoMapper) {
        this.giftCertificateDao = giftCertificateDao;
        this.requestGiftCertificateDtoMapper = requestGiftCertificateDtoMapper;
        this.responseGiftCertificateDtoMapper = responseGiftCertificateDtoMapper;
        this.tagService = tagService;
        this.paginationContextBuilder = paginationContextBuilder;
        this.responseTagDtoMapper = responseTagDtoMapper;
    }

    @Override
    @Transactional
    public ResponseGiftCertificateDto createGiftCertificate(RequestGiftCertificateDto requestGiftCertificateDto) {
        Optional<GiftCertificate> foundByNameCertificate = giftCertificateDao.findGiftCertificateByName
                (requestGiftCertificateDto.getName());
        if (foundByNameCertificate.isPresent()) {
            throw new ResourceAlreadyExistsException(ResourceBundleErrorMessage.CERTIFICATE_ALREADY_EXISTS,
                    foundByNameCertificate.get().getId());
        }
        GiftCertificate giftCertificateForCreation = requestGiftCertificateDtoMapper.toModel(requestGiftCertificateDto);
        List<Tag> identifiedTags = identifyGiftCertificatesTags(requestGiftCertificateDto);
        giftCertificateForCreation.setTags(identifiedTags);
        setCreateAndUpdateDate(giftCertificateForCreation);
        return responseGiftCertificateDtoMapper.toDto(giftCertificateDao.create(giftCertificateForCreation));
    }

    @Override
    public ResponseGiftCertificateDto findGiftCertificateById(Integer id) {
        return responseGiftCertificateDtoMapper.toDto(giftCertificateDao.findById(id));
    }

    @Override
    @Transactional
    public boolean deleteGiftCertificate(Integer id) {
        return giftCertificateDao.delete(id);
    }

    @Override
    @Transactional
    public ResponseGiftCertificateDto updateGiftCertificate(RequestGiftCertificateDto requestGiftCertificateDto, Integer id) {
        Optional<GiftCertificate> foundByNameCertificate = giftCertificateDao.findGiftCertificateByName
                (requestGiftCertificateDto.getName());
        GiftCertificate existingCertificate = giftCertificateDao.findById(id);
        if (foundByNameCertificate.isPresent() && !foundByNameCertificate.get().getId().equals(existingCertificate.getId())) {
            throw new ResourceAlreadyExistsException(ResourceBundleErrorMessage.CERTIFICATE_ALREADY_EXISTS,
                    foundByNameCertificate.get().getId());
        }
        GiftCertificate giftCertificateForUpdate = requestGiftCertificateDtoMapper.toModel(requestGiftCertificateDto);
        if (nonNull(requestGiftCertificateDto.getTags())) {
            List<Tag> identifiedTags = identifyGiftCertificatesTags(requestGiftCertificateDto);
            List<Tag> existingTags = existingCertificate.getTags();
            existingTags.addAll(identifiedTags);
            giftCertificateForUpdate.setTags(removeDuplicateTags(existingTags));
        }
        existingCertificate.merge(giftCertificateForUpdate);
        existingCertificate.setLastUpdateDate(LocalDateTime.now());
        return responseGiftCertificateDtoMapper.toDto(giftCertificateDao.update(existingCertificate));
    }

    @Override
    public List<ResponseGiftCertificateDto> findAllGiftCertificates(GiftCertificateCriteria giftCertificateCriteria,
                                                                    Pagination pagination) {
        int startOfRecords = paginationContextBuilder.defineStartOfRecords(pagination);
        int recordsPerPageAmount = paginationContextBuilder.defineRecordsPerPageAmount(pagination.getSize());
        return giftCertificateDao.findAll(giftCertificateCriteria, startOfRecords, recordsPerPageAmount).stream().
                map(responseGiftCertificateDtoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Integer findQuantity(GiftCertificateCriteria giftCertificateCriteria) {
        return giftCertificateDao.findQuantity(giftCertificateCriteria);
    }

    private List<Tag> removeDuplicateTags(List<Tag> listWithDuplicates) {
        Set<Tag> uniqueTags = new HashSet<>(listWithDuplicates);
        return new ArrayList<>(uniqueTags);
    }

    private List<Tag> identifyGiftCertificatesTags(RequestGiftCertificateDto requestGiftCertificateDto) {
        List<Tag> identifiedGiftCertificateTags = requestGiftCertificateDto.getTags().stream().map(tag -> {
            if (tagService.findTagByName(tag.getName()).isPresent()) {
                return tagService.findTagByName(tag.getName()).get();
            } else {
                return responseTagDtoMapper.toModel(tagService.createTag(tag));
            }
        }).collect(Collectors.toList());
        return identifiedGiftCertificateTags;
    }

    private GiftCertificate setCreateAndUpdateDate(GiftCertificate giftCertificate) {
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        return giftCertificate;
    }
}
