package com.epam.esm.controller;

import com.epam.esm.dto.ResponsePurchaseDto;
import com.epam.esm.service.PurchaseService;
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
 * {@code Purchase} data which belongs to a specific {@code User}
 */
@RestController
@RequestMapping("users/{id}/purchases")
public class UserPurchaseController {

    private final PurchaseService purchaseService;
    private final NavigationLinkBuilder<ResponsePurchaseDto> responsePurchaseDtoLinkBuilder;
    private static final String LINKS_FIELD = "links";

    public UserPurchaseController(PurchaseService purchaseService, NavigationLinkBuilder<ResponsePurchaseDto> responsePurchaseDtoLinkBuilder) {
        this.purchaseService = purchaseService;
        this.responsePurchaseDtoLinkBuilder = responsePurchaseDtoLinkBuilder;
    }

    /**
     * Finds users purchases with fields, passed in request.
     *
     * @param id     {@code User}'s id
     * @param fields optional request param, fields that should be passed in response
     * @return {@code List<Map>} which represents found purchases.
     */
    @GetMapping
    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #id) or hasRole('ROLE_ADMIN')")
    public CollectionModel<ResponsePurchaseDto> findUsersPurchases(@PathVariable Integer id, @RequestParam(required = false) Set<String> fields) {
        List<ResponsePurchaseDto> usersPurchases = purchaseService.findPurchasesByUserId(id);
        List<ResponsePurchaseDto> usersPurchasesWithHateoasSupport =
                usersPurchases.stream().map(responsePurchaseDtoLinkBuilder::buildLinks).collect(Collectors.toList());
        return CollectionModel.of(buildResponseAccordingIncomingFields(fields, usersPurchasesWithHateoasSupport));
    }

    private List<ResponsePurchaseDto> buildResponseAccordingIncomingFields(Set<String> fields, List<ResponsePurchaseDto> purchases) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map> purchasesWithRequestFields = purchases.stream().map(
                purchase -> {
                    Map map = objectMapper.convertValue(purchase, Map.class);
                    if (nonNull(fields)) {
                        fields.add(LINKS_FIELD);
                        map.keySet().retainAll(fields);
                    }
                    return map;
                }).collect(Collectors.toList());
        return purchasesWithRequestFields.stream().map(map -> objectMapper.convertValue(map, ResponsePurchaseDto.class))
                .collect(Collectors.toList());
    }
}