package com.epam.esm.service;

import com.epam.esm.dto.RequestTagDto;
import com.epam.esm.dto.ResponseTagDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * {@code Tag} service interface.
 */
public interface TagService {

    /**
     * Finds tag by id.
     *
     * @param id {@code Tag}'s id.
     * @return {@code ResponseTagDto} which represents found tag.
     */
    ResponseTagDto findTagById(Integer id);

    /**
     * Creates tag.
     *
     * @param requestTagDto {@code RequestTagDto} for creation.
     * @return Created tag which is represented by {@code ResponseTagDto}.
     * @throws ResourceAlreadyExistsException if tag with the same name is already exists.
     */
    ResponseTagDto createTag(RequestTagDto requestTagDto);

    /**
     * Deletes tag by its id.
     *
     * @param tagId {@code Tag}'s id.
     * @return {@code true} if tag was successfully deleted.
     * @throws ResourceNotFoundException if tag with such id is not exist.
     */
    boolean deleteTag(Integer tagId);

    /**
     * Finds tag by its name.
     *
     * @param tagName {@code Tag}'s name.
     * @return {@code Optional<Tag>} found by name tag.
     */
    Optional<Tag> findTagByName(String tagName);

    /**
     * Finds all tags corresponding to given page, number of records per page.
     *
     * @param pagination {@code Pagination} which contains page number and records per page amount.
     * @return found tags.
     */
    List<ResponseTagDto> findAll(Pagination pagination);

    /**
     * Finds the most widely used tag of a user with the highest cost of all orders.
     *
     * @return {@code ResponseTagDto} found tag.
     */
    ResponseTagDto findTheMostPopularTagInUserWithTheHighestCostOfOrders();

    /**
     * Finds quantity of all tags.
     *
     * @return {@code Long} quantity
     */
    Long findQuantity();
}
