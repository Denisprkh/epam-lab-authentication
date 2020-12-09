package com.epam.esm.service.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dto.RequestTagDto;
import com.epam.esm.dto.ResponseTagDto;
import com.epam.esm.dto.mapper.impl.RequestTagDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseTagDtoMapper;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.service.TagService;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final RequestTagDtoMapper requestTagDtoMapper;
    private final PaginationContextBuilder paginationContextBuilder;
    private final ResponseTagDtoMapper responseTagDtoMapper;

    public TagServiceImpl(TagDao tagDao, RequestTagDtoMapper requestTagDtoMapper, PaginationContextBuilder paginationContextBuilder,
                          ResponseTagDtoMapper responseTagDtoMapper) {
        this.tagDao = tagDao;
        this.requestTagDtoMapper = requestTagDtoMapper;
        this.paginationContextBuilder = paginationContextBuilder;
        this.responseTagDtoMapper = responseTagDtoMapper;
    }

    @Override
    public ResponseTagDto findTagById(Integer id) {
        return responseTagDtoMapper.toDto(tagDao.findById(id));
    }

    @Override
    @Transactional
    public ResponseTagDto createTag(RequestTagDto requestTagDto) {
        Optional<Tag> tagFoundByName = findTagByName(requestTagDto.getName());
        if (tagFoundByName.isPresent()) {
            throw new ResourceAlreadyExistsException(ResourceBundleErrorMessage.TAG_IS_ALREADY_EXISTS,
                    tagFoundByName.get().getId());
        }
        return responseTagDtoMapper.toDto(tagDao.create(requestTagDtoMapper.toModel(requestTagDto)));
    }

    @Override
    @Transactional
    public boolean deleteTag(Integer tagId) {
        boolean giftCertificateTagIsDeleted = tagDao.deleteGiftCertificateTag(tagId);
        boolean tagIsDeleted = tagDao.delete(tagId);
        return giftCertificateTagIsDeleted && tagIsDeleted;
    }

    @Override
    public Optional<Tag> findTagByName(String tagName) {
        return tagDao.findTagByName(tagName);
    }

    @Override
    public List<ResponseTagDto> findAll(Pagination pagination) {
        int startOfRecords = paginationContextBuilder.defineStartOfRecords(pagination);
        int recordsPerPageAmount = paginationContextBuilder.defineRecordsPerPageAmount(pagination.getSize());
        return tagDao.findAll(startOfRecords, recordsPerPageAmount).stream().map(responseTagDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseTagDto findTheMostPopularTagInUserWithTheHighestCostOfOrders() {
        return responseTagDtoMapper.toDto(tagDao.findTheMostPopularTagInUserWithTheHighestCostOfOrders());
    }

    @Override
    public Long findQuantity() {
        return tagDao.findQuantity();
    }

}
