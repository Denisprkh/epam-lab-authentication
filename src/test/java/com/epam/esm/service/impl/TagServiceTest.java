package com.epam.esm.service.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dto.RequestTagDto;
import com.epam.esm.dto.ResponseTagDto;
import com.epam.esm.dto.mapper.impl.RequestTagDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseTagDtoMapper;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    private static final String DEFAULT_NAME = "name";
    private static final Integer DEFAULT_ID = 1;

    @Mock
    private TagDao tagDao;

    @Mock
    private RequestTagDtoMapper requestTagDtoMapper;

    @Mock
    private PaginationContextBuilder paginationContextBuilder;

    @Mock
    private ResponseTagDtoMapper responseTagDtoMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findTagByIdTest() {
        Tag tag = new Tag(DEFAULT_ID, DEFAULT_NAME);
        ResponseTagDto expectedTagDto = new ResponseTagDto();
        expectedTagDto.setId(DEFAULT_ID);
        expectedTagDto.setName(DEFAULT_NAME);

        when(tagDao.findById(DEFAULT_ID)).thenReturn(tag);
        when(responseTagDtoMapper.toDto(tag)).thenReturn(expectedTagDto);
        ResponseTagDto resultTag = tagService.findTagById(DEFAULT_ID);

        assertEquals(expectedTagDto, resultTag);
    }

    @Test
    void findTagByIdTestShouldThrowResourceNotFoundException() {
        when(tagDao.findById(DEFAULT_ID)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> tagService.findTagById(DEFAULT_ID));
    }

    @Test
    void findTagByNameTestExistingTag() {
        Tag expectedTag = new Tag(DEFAULT_NAME);

        when(tagDao.findTagByName(DEFAULT_NAME)).thenReturn(Optional.of(expectedTag));
        Tag resultTag = tagService.findTagByName(DEFAULT_NAME).get();

        assertEquals(expectedTag, resultTag);
    }

    @Test
    void findTagByNameTestNotExistingTag() {
        when(tagDao.findTagByName(DEFAULT_NAME)).thenReturn(Optional.empty());
        Optional<Tag> resultTag = tagService.findTagByName(DEFAULT_NAME);

        assertEquals(Boolean.FALSE, resultTag.isPresent());
    }

    @Test
    void createTagTest() {
        RequestTagDto requestTagDto = new RequestTagDto();
        requestTagDto.setName(DEFAULT_NAME);
        Tag returnedDaoTag = new Tag();
        returnedDaoTag.setId(DEFAULT_ID);
        returnedDaoTag.setName(DEFAULT_NAME);
        Tag tagForCreation = new Tag(DEFAULT_NAME);
        ResponseTagDto expectedTag = new ResponseTagDto();
        expectedTag.setName(DEFAULT_NAME);
        expectedTag.setId(DEFAULT_ID);

        when(requestTagDtoMapper.toModel(requestTagDto)).thenReturn(tagForCreation);
        when(tagDao.create(tagForCreation)).thenReturn(returnedDaoTag);
        when(responseTagDtoMapper.toDto(returnedDaoTag)).thenReturn(expectedTag);
        ResponseTagDto resultTag = tagService.createTag(requestTagDto);

        assertEquals(expectedTag, resultTag);
    }

    @Test
    void createTagTestWithExistingNameShouldThrowException() {
        RequestTagDto requestTagDto = new RequestTagDto();
        requestTagDto.setName(DEFAULT_NAME);
        Tag existingTag = new Tag();
        existingTag.setId(DEFAULT_ID);
        existingTag.setName(DEFAULT_NAME);

        when(tagDao.findTagByName(DEFAULT_NAME)).thenReturn(Optional.of(existingTag));

        assertThrows(ResourceAlreadyExistsException.class, () -> tagService.createTag(requestTagDto));
    }

    @Test
    void deleteTagTest() {
        when(tagDao.delete(DEFAULT_ID)).thenReturn(Boolean.TRUE);
        when(tagDao.deleteGiftCertificateTag(DEFAULT_ID)).thenReturn(Boolean.TRUE);

        assertTrue(tagService.deleteTag(DEFAULT_ID));
    }

    @Test()
    void deleteTagShouldThrowException() {
        when(tagDao.delete(DEFAULT_ID)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> tagService.deleteTag(DEFAULT_ID));
    }

    @Test
    void findQuantityTest() {
        when(tagDao.findQuantity()).thenReturn(10L);
        long expectedValue = 10L;
        long resultValue = tagService.findQuantity();

        assertEquals(expectedValue, resultValue);
    }

    @Test
    void findAllTagsTest() {
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        Tag anotherTag = new Tag();
        tags.add(tag);
        tags.add(anotherTag);
        List<ResponseTagDto> expectedTags = new ArrayList<>();
        expectedTags.add(new ResponseTagDto());
        expectedTags.add(new ResponseTagDto());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "2");
        Pagination pagination = buildPagination();

        when(tagDao.findAll(0, 2)).thenReturn(tags);
        when(responseTagDtoMapper.toDto(any())).thenReturn(new ResponseTagDto());
        when(paginationContextBuilder.defineRecordsPerPageAmount(2)).thenReturn(2);
        when(paginationContextBuilder.defineStartOfRecords(pagination)).thenReturn(0);
        int expectedTagsSize = expectedTags.size();
        int resultTagsSize = tagService.findAll(pagination).size();

        assertEquals(expectedTagsSize, resultTagsSize);

    }

    private Pagination buildPagination() {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(2);
        return pagination;
    }
}
