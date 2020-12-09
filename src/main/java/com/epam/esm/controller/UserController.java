package com.epam.esm.controller;

import com.epam.esm.dto.ResponseUserDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.UserService;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import com.epam.esm.service.util.pagination.PaginationResourceResolver;
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
 * Controller used to manipulate read operations on
 * {@code User} data
 */
@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final NavigationLinkBuilder<ResponseUserDto> responseUserDtoLinkBuilder;
    private final PaginationResourceResolver paginationResourceResolver;

    public UserController(UserService userService, NavigationLinkBuilder<ResponseUserDto> responseUserDtoLinkBuilder,
                          PaginationResourceResolver paginationResourceResolver) {
        this.userService = userService;
        this.responseUserDtoLinkBuilder = responseUserDtoLinkBuilder;
        this.paginationResourceResolver = paginationResourceResolver;
    }

    /**
     * Find order by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id {@code User}'s id.
     * @return found user.
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseUserDto> findUserById(@PathVariable Integer id) {
        ResponseUserDto foundByIdUser = userService.findUserById(id);
        return new ResponseEntity<>(responseUserDtoLinkBuilder.buildLinks(foundByIdUser), HttpStatus.OK);
    }

    /**
     * Returns all users corresponding to given page and number of records per page.
     *
     * @param pagination page number and page size are stored there.
     * @param params     for storing page info in hateoas.
     * @return found users.
     */
    @GetMapping
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public RepresentationModel<?> findAllUsers(@Valid Pagination pagination,
                                               @RequestParam Map<String, String> params) {
        long totalRecordsNumber = userService.findQuantity();
        List<ResponseUserDto> users = userService.findAllUsers(pagination).stream().map(responseUserDtoLinkBuilder::buildLinks)
                .collect(Collectors.toList());
        Map<String, Long> pageInfo = paginationResourceResolver.constructPageInfoStructure(pagination, totalRecordsNumber);
        List<Link> links = paginationResourceResolver.buildPaginationLinks(methodOn(UserController.class).findAllUsers(pagination,
                params), pagination, totalRecordsNumber, params);
        CollectionModel<ResponseUserDto> collectionModel = CollectionModel.of(users);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(pageInfo, LinkRelation.of(PaginationParameter.PARAM_PAGE)).build();
    }
}
