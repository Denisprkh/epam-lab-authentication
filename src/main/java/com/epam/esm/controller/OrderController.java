package com.epam.esm.controller;

import com.epam.esm.dto.RequestOrderDto;
import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.OrderService;
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
 * Controller used to manipulate CR operations on
 * {@code Order} data
 */
@RestController
@RequestMapping("orders")
public class OrderController {

    private final OrderService orderService;
    private final NavigationLinkBuilder<ResponseOrderDto> responseOrderDtoLinkBuilder;
    private final PaginationResourceResolver paginationResourceResolver;

    public OrderController(OrderService orderService, NavigationLinkBuilder<ResponseOrderDto> responseOrderDtoLinkBuilder,
                           PaginationResourceResolver paginationResourceResolver) {
        this.orderService = orderService;
        this.responseOrderDtoLinkBuilder = responseOrderDtoLinkBuilder;
        this.paginationResourceResolver = paginationResourceResolver;
    }

    /**
     * Finds order by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id {@code Order}'s id.
     * @return created order.
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "@userSecurity.isUsersOrder(authentication, #id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseOrderDto> findOrderById(@PathVariable Integer id) {
        ResponseOrderDto foundByIdOrder = orderService.findOrderById(id);
        return new ResponseEntity<>(responseOrderDtoLinkBuilder.buildLinks(foundByIdOrder), HttpStatus.OK);
    }

    /**
     * Returns all orders corresponding to given page and number of records per page.
     *
     * @param pagination page number and page size are stored there.
     * @param params     for storing page info in hateoas.
     * @return found orders.
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RepresentationModel<?> findAllOrders(@Valid Pagination pagination, @RequestParam Map<String, String> params) {
        long totalRecordsNumber = orderService.findQuantity();
        List<ResponseOrderDto> orders = orderService.findAllOrders(pagination).stream().map(responseOrderDtoLinkBuilder::buildLinks)
                .collect(Collectors.toList());
        Map<String, Long> pageInfo = paginationResourceResolver.constructPageInfoStructure(pagination, totalRecordsNumber);
        List<Link> links = paginationResourceResolver.buildPaginationLinks(methodOn(OrderController.class).findAllOrders(pagination, params),
                pagination, totalRecordsNumber, params);
        CollectionModel<ResponseOrderDto> collectionModel = CollectionModel.of(orders);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(pageInfo, LinkRelation.of(PaginationParameter.PARAM_PAGE)).build();
    }

    /**
     * Creates order.
     * If passed certificates ids or users id don't exists {@code HttpStatus.BAD_REQUEST} is returned.
     *
     * @param requestOrderDto {@code RequestOrderDto} with values for {@code Order} to create.
     * @return created order.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponseOrderDto> createOrder(@Valid @RequestBody RequestOrderDto requestOrderDto) {
        ResponseOrderDto createdOrder = orderService.createOrder(requestOrderDto);
        return new ResponseEntity<>(responseOrderDtoLinkBuilder.buildLinks(createdOrder),
                HttpStatus.CREATED);
    }


}
