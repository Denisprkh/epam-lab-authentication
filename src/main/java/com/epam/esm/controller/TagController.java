package com.epam.esm.controller;

import com.epam.esm.entity.Pagination;
import com.epam.esm.service.util.pagination.PaginationResourceResolver;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import com.epam.esm.dto.RequestTagDto;
import com.epam.esm.dto.ResponseTagDto;
import com.epam.esm.service.TagService;
import com.epam.esm.util.PaginationParameter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.hal.HalModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller used to manipulate CRD operations on
 * {@code Tag} data
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final NavigationLinkBuilder<ResponseTagDto> responseTagDtoLinkBuilder;
    private final PaginationResourceResolver paginationResourceResolver;

    public TagController(TagService tagService, NavigationLinkBuilder<ResponseTagDto> responseTagDtoLinkBuilder,
                         PaginationResourceResolver paginationResourceResolver) {
        this.tagService = tagService;
        this.responseTagDtoLinkBuilder = responseTagDtoLinkBuilder;
        this.paginationResourceResolver = paginationResourceResolver;
    }

    /**
     * Finds tag by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id {@code Tag}'s id.
     * @return found tag.
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseTagDto> findTagById(@PathVariable Integer id) {
        ResponseTagDto foundByIdTag = tagService.findTagById(id);
        return new ResponseEntity<>(responseTagDtoLinkBuilder.buildLinks(foundByIdTag), HttpStatus.OK);
    }

    /**
     * Creates a new Tag in the database.
     * If resource is already exists {@code HttpStatus.BAD_REQUEST} is returned.
     *
     * @param requestTagDto {@code RequestTagDto} which represents tag to create.
     * @return created tag.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseTagDto> createTag(@Valid @RequestBody RequestTagDto requestTagDto) {
        ResponseTagDto createdTag = tagService.createTag(requestTagDto);
        return new ResponseEntity<>(responseTagDtoLinkBuilder.buildLinks(createdTag), HttpStatus.CREATED);
    }

    /**
     * Deletes tag with the specified id.
     * If no resources with such id are exists {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id {@code Tag}'s id.
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTag(@PathVariable Integer id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Returns all tags corresponding to given page and number of records per page.
     *
     * @param pagination page number and page size are stored there.
     * @param params     for storing page info in hateoas.
     * @return found tags.
     */
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public RepresentationModel<?> findAllTags(@Valid Pagination pagination,
                                              @RequestParam Map<String, String> params) {
        long tagsCount = tagService.findQuantity();
        List<ResponseTagDto> tags = tagService.findAll(pagination).stream().map(responseTagDtoLinkBuilder::buildLinks)
                .collect(Collectors.toList());
        Map<String, Long> pageInfo = paginationResourceResolver.constructPageInfoStructure(pagination, tagsCount);
        List<Link> links = paginationResourceResolver.buildPaginationLinks(
                methodOn(TagController.class).findAllTags(pagination, params), pagination, tagsCount, params);
        CollectionModel<ResponseTagDto> collectionModel = CollectionModel.of(tags);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(pageInfo, LinkRelation.of(PaginationParameter.PARAM_PAGE)).build();
    }

    /**
     * Finds the most widely used tag of a user with the highest cost of all purchases.
     *
     * @return found tag.
     */
    @GetMapping(value = "most-paid-popular")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseTagDto> findTheMostPopularTagInUserWithHighestSumOfPurchases() {
        ResponseTagDto mostPaidPopularTag = tagService.findTheMostPopularTagInUserWithTheHighestCostOfPurchases();
        return new ResponseEntity<>(responseTagDtoLinkBuilder.buildLinks(mostPaidPopularTag), HttpStatus.OK);
    }

}
