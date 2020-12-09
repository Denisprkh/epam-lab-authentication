package com.epam.esm.controller;

import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.util.linkbuilder.NavigationLinkBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller used to manipulate read operations on
 * {@code Order} data which belongs to a specific {@code User}
 */
@RestController
@RequestMapping("users/{id}/orders")
public class UserOrderController {

    private final OrderService orderService;
    private final NavigationLinkBuilder<ResponseOrderDto> responseOrderDtoLinkBuilder;
    private static final String LINKS_FIELD = "links";

    public UserOrderController(OrderService orderService, NavigationLinkBuilder<ResponseOrderDto> responseOrderDtoLinkBuilder) {
        this.orderService = orderService;
        this.responseOrderDtoLinkBuilder = responseOrderDtoLinkBuilder;
    }

    /**
     * Finds users orders with fields, passed in request.
     *
     * @param id     {@code User}'s id
     * @param fields optional request param, fields that should be passed in response
     * @return {@code List<Map>} which represents found orders.
     */
    @GetMapping
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #id) or hasRole('ROLE_ADMIN')")
    public CollectionModel<ResponseOrderDto> findUsersOrders(@PathVariable Integer id, @RequestParam(required = false) Set<String> fields) {
        List<ResponseOrderDto> usersOrders = orderService.findOrdersByUserId(id);
        List<ResponseOrderDto> usersOrdersWithHateoasSupport =
                usersOrders.stream().map(responseOrderDtoLinkBuilder::buildLinks).collect(Collectors.toList());
        return CollectionModel.of(buildResponseAccordingIncomingFields(fields, usersOrdersWithHateoasSupport));
    }

    private List<ResponseOrderDto> buildResponseAccordingIncomingFields(Set<String> fields, List<ResponseOrderDto> orders) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> ordersWithRequestFields = orders.stream().map(
                order -> {
                    Map map = objectMapper.convertValue(order, Map.class);
                    if (nonNull(fields)) {
                        fields.add(LINKS_FIELD);
                        map.keySet().retainAll(fields);
                    }
                    return map;
                }).collect(Collectors.toList());
        return ordersWithRequestFields.stream().map(map -> objectMapper.convertValue(map, ResponseOrderDto.class))
                .collect(Collectors.toList());
    }
}