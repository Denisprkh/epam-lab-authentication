package com.epam.esm.service.impl;

import com.epam.esm.dao.giftcertificate.GiftCertificateDao;
import com.epam.esm.dto.RequestGiftCertificateDto;
import com.epam.esm.dto.RequestTagDto;
import com.epam.esm.dto.ResponseGiftCertificateDto;
import com.epam.esm.dto.mapper.impl.RequestGiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseGiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseTagDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.dao.util.GiftCertificateCriteria;
import com.epam.esm.controller.util.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

    private static final String DEFAULT_NAME = "name";
    private static final Integer DEFAULT_ID = 1;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private RequestGiftCertificateDtoMapper requestGiftCertificateDtoMapper;

    @Mock
    private ResponseGiftCertificateDtoMapper responseGiftCertificateDtoMapper;

    @Mock
    private TagService tagService;

    @Mock
    private PaginationContextBuilder paginationContextBuilder;

    @Mock
    private ResponseTagDtoMapper responseTagDtoMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;


    @Test
    void createGiftCertificateTest() {
        RequestGiftCertificateDto requestGiftCertificateDto = buildRequestGiftCertificateDto();
        GiftCertificate mappedToModelCertificate = new GiftCertificate();
        mappedToModelCertificate.setName(DEFAULT_NAME);
        List<Tag> mappedToModelCertificatesTags = new ArrayList<>();
        mappedToModelCertificatesTags.add(new Tag(DEFAULT_NAME));
        mappedToModelCertificate.setTags(mappedToModelCertificatesTags);
        GiftCertificate returnedDaoGiftCertificate = new GiftCertificate();
        returnedDaoGiftCertificate.setId(DEFAULT_ID);
        returnedDaoGiftCertificate.setName(DEFAULT_NAME);
        ResponseGiftCertificateDto expectedGiftCertificate = new ResponseGiftCertificateDto();
        expectedGiftCertificate.setId(DEFAULT_ID);
        expectedGiftCertificate.setName(DEFAULT_NAME);

        when(giftCertificateDao.create(any(GiftCertificate.class))).thenReturn(returnedDaoGiftCertificate);
        when(requestGiftCertificateDtoMapper.toModel(requestGiftCertificateDto)).thenReturn(mappedToModelCertificate);
        when(responseGiftCertificateDtoMapper.toDto(returnedDaoGiftCertificate)).thenReturn(expectedGiftCertificate);
        when(tagService.findTagByName(anyString())).thenReturn(Optional.empty());
        ResponseGiftCertificateDto resultGiftCertificate = giftCertificateService.createGiftCertificate(requestGiftCertificateDto);

        assertEquals(expectedGiftCertificate, resultGiftCertificate);

    }

    @Test
    void createTagShouldThrowException() {
        RequestGiftCertificateDto giftCertificateDtoWithExistingName = new RequestGiftCertificateDto();
        giftCertificateDtoWithExistingName.setName("existingName");
        GiftCertificate existingCertificate = new GiftCertificate();
        existingCertificate.setId(DEFAULT_ID);
        existingCertificate.setName("existingName");

        when(giftCertificateDao.findGiftCertificateByName("existingName"))
                .thenReturn(Optional.of(existingCertificate));

        assertThrows(ResourceAlreadyExistsException.class, () -> giftCertificateService
                .createGiftCertificate(giftCertificateDtoWithExistingName));
    }

    @Test
    void findGiftCertificateByIdTest() {
        GiftCertificate foundByIdCertificate = new GiftCertificate();
        foundByIdCertificate.setId(DEFAULT_ID);
        ResponseGiftCertificateDto expectedCertificate = new ResponseGiftCertificateDto();
        expectedCertificate.setId(DEFAULT_ID);

        when(giftCertificateDao.findById(DEFAULT_ID)).thenReturn(foundByIdCertificate);
        when(responseGiftCertificateDtoMapper.toDto(any(GiftCertificate.class))).thenReturn(expectedCertificate);
        ResponseGiftCertificateDto resultCertificate = giftCertificateService.findGiftCertificateById(DEFAULT_ID);

        assertEquals(expectedCertificate, resultCertificate);

    }

    @Test
    void findGiftCertificateByIdTestShouldThrowException() {
        when(giftCertificateDao.findById(DEFAULT_ID)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.findGiftCertificateById(DEFAULT_ID));
    }

    @Test
    void deleteGiftCertificateTest() {
        when(giftCertificateDao.delete(DEFAULT_ID)).thenReturn(Boolean.TRUE);

        assertEquals(Boolean.TRUE, giftCertificateService.deleteGiftCertificate(DEFAULT_ID));
    }

    @Test
    void deleteGiftCertificateShouldThrowException() {
        when(giftCertificateDao.delete(DEFAULT_ID)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.deleteGiftCertificate(DEFAULT_ID));
    }

    @Test
    void updateGiftCertificateTest() {
        RequestGiftCertificateDto certificateDtoWithUpdatedValues = new RequestGiftCertificateDto();
        certificateDtoWithUpdatedValues.setName("newName");
        int certificateId = DEFAULT_ID;
        GiftCertificate existingGiftCertificate = new GiftCertificate();
        existingGiftCertificate.setId(certificateId);
        existingGiftCertificate.setName(DEFAULT_NAME);
        GiftCertificate giftCertificateWithUpdatedValues = new GiftCertificate();
        giftCertificateWithUpdatedValues.setId(certificateId);
        giftCertificateWithUpdatedValues.setName("newName");
        GiftCertificate updatedGiftCertificate = new GiftCertificate();
        updatedGiftCertificate.setId(certificateId);
        updatedGiftCertificate.setName("newName");
        ResponseGiftCertificateDto expectedCertificate = new ResponseGiftCertificateDto();
        expectedCertificate.setId(certificateId);
        expectedCertificate.setName("newName");

        when(giftCertificateDao.findById(certificateId)).thenReturn(existingGiftCertificate);
        when(requestGiftCertificateDtoMapper.toModel(certificateDtoWithUpdatedValues)).thenReturn(giftCertificateWithUpdatedValues);
        when(giftCertificateDao.update(existingGiftCertificate)).thenReturn(updatedGiftCertificate);
        when(responseGiftCertificateDtoMapper.toDto(updatedGiftCertificate)).thenReturn(expectedCertificate);
        ResponseGiftCertificateDto resultCertificate = giftCertificateService.updateGiftCertificate
                (certificateDtoWithUpdatedValues, certificateId);

        assertEquals(expectedCertificate, resultCertificate);

    }

    @Test
    void updateGiftCertificateTestNotExistingCertificateShouldThrowException() {
        RequestGiftCertificateDto certificateDtoWithUpdatedValues = new RequestGiftCertificateDto();
        certificateDtoWithUpdatedValues.setName("newName");
        int certificateId = 1;

        when(giftCertificateDao.findById(1)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> giftCertificateService.updateGiftCertificate
                (certificateDtoWithUpdatedValues, certificateId));
    }

    @Test
    void updateGiftCertificateTestDuplicateNameShouldThrowException() {
        RequestGiftCertificateDto certificateDtoWithUpdatedValues = new RequestGiftCertificateDto();
        certificateDtoWithUpdatedValues.setName("newName");
        GiftCertificate existingGiftCertificate = new GiftCertificate();
        int existingCertificateId = 2;
        existingGiftCertificate.setId(existingCertificateId);
        existingGiftCertificate.setName(DEFAULT_NAME);
        int certificateId = DEFAULT_ID;
        GiftCertificate giftCertificateForUpdate = new GiftCertificate();
        giftCertificateForUpdate.setId(certificateId);

        when(giftCertificateDao.findGiftCertificateByName(certificateDtoWithUpdatedValues.getName()))
                .thenReturn(Optional.of(existingGiftCertificate));
        when(giftCertificateDao.findById(certificateId)).thenReturn(giftCertificateForUpdate);

        assertThrows(ResourceAlreadyExistsException.class, () -> giftCertificateService.updateGiftCertificate
                (certificateDtoWithUpdatedValues, certificateId));
    }

    @Test
    void findAllGiftCertificatesTest() {
        List<GiftCertificate> giftCertificates = new ArrayList<>();
        GiftCertificate giftCertificate = new GiftCertificate();
        GiftCertificate anotherGiftCertificate = new GiftCertificate();
        giftCertificates.add(giftCertificate);
        giftCertificates.add(anotherGiftCertificate);
        List<ResponseGiftCertificateDto> expectedCertificates = new ArrayList<>();
        expectedCertificates.add(new ResponseGiftCertificateDto());
        expectedCertificates.add(new ResponseGiftCertificateDto());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "2");
        int expectedCertificatesSize = expectedCertificates.size();
        Pagination pagination = buildPagination();

        when(giftCertificateDao.findAll(new GiftCertificateCriteria(), 0, 2)).thenReturn(giftCertificates);
        when(responseGiftCertificateDtoMapper.toDto(any(GiftCertificate.class))).thenReturn(new ResponseGiftCertificateDto());
        when(paginationContextBuilder.defineRecordsPerPageAmount(2)).thenReturn(2);
        when(paginationContextBuilder.defineStartOfRecords(pagination)).thenReturn(0);
        int resultCertificatesSize = giftCertificateService.findAllGiftCertificates(new GiftCertificateCriteria(), pagination).size();

        assertEquals(expectedCertificatesSize, resultCertificatesSize);

    }

    private RequestGiftCertificateDto buildRequestGiftCertificateDto() {
        RequestGiftCertificateDto requestGiftCertificateDto = new RequestGiftCertificateDto();
        requestGiftCertificateDto.setName(DEFAULT_NAME);
        List<RequestTagDto> requestGiftCertificateTags = new ArrayList<>();
        requestGiftCertificateTags.add(new RequestTagDto(DEFAULT_NAME));
        requestGiftCertificateDto.setTags(requestGiftCertificateTags);
        return requestGiftCertificateDto;
    }

    private Pagination buildPagination() {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(2);
        return pagination;
    }

}
