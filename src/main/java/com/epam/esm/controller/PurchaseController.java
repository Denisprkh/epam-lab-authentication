package com.epam.esm.controller;

import com.epam.esm.dto.RequestPurchaseDto;
import com.epam.esm.dto.ResponsePurchaseDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.service.PurchaseService;
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
 * {@code Purchase} data
 */
@RestController
@RequestMapping("purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final NavigationLinkBuilder<ResponsePurchaseDto> responsePurchaseDtoLinkBuilder;
    private final PaginationResourceResolver paginationResourceResolver;

    public PurchaseController(PurchaseService purchaseService, NavigationLinkBuilder<ResponsePurchaseDto> responsePurchaseDtoLinkBuilder,
                              PaginationResourceResolver paginationResourceResolver) {
        this.purchaseService = purchaseService;
        this.responsePurchaseDtoLinkBuilder = responsePurchaseDtoLinkBuilder;
        this.paginationResourceResolver = paginationResourceResolver;
    }

    /**
     * Finds purchase by id.
     * If no resource found {@code HttpStatus.NOT_FOUND} is returned.
     *
     * @param id {@code Purchase}'s id.
     * @return created purchase.
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize(value = "@userSecurity.isUsersPurchase(authentication, #id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponsePurchaseDto> findPurchaseById(@PathVariable Integer id) {
        ResponsePurchaseDto foundByIdPurchase = purchaseService.findPurchaseById(id);
        return new ResponseEntity<>(responsePurchaseDtoLinkBuilder.buildLinks(foundByIdPurchase), HttpStatus.OK);
    }

    /**
     * Returns all purchases corresponding to given page and number of records per page.
     *
     * @param pagination page number and page size are stored there.
     * @param params     for storing page info in hateoas.
     * @return found purchases.
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RepresentationModel<?> findAllPurchases(@Valid Pagination pagination, @RequestParam Map<String, String> params) {
        long totalRecordsNumber = purchaseService.findQuantity();
        List<ResponsePurchaseDto> purchases = purchaseService.findAllPurchases(pagination).stream().map(responsePurchaseDtoLinkBuilder::buildLinks)
                .collect(Collectors.toList());
        Map<String, Long> pageInfo = paginationResourceResolver.constructPageInfoStructure(pagination, totalRecordsNumber);
        List<Link> links = paginationResourceResolver.buildPaginationLinks(methodOn(PurchaseController.class).findAllPurchases(pagination, params),
                pagination, totalRecordsNumber, params);
        CollectionModel<ResponsePurchaseDto> collectionModel = CollectionModel.of(purchases);
        return HalModelBuilder.halModelOf(collectionModel).links(links).embed(pageInfo, LinkRelation.of(PaginationParameter.PARAM_PAGE)).build();
    }

    /**
     * Creates purchase.
     * If passed certificates ids or users id don't exists {@code HttpStatus.BAD_REQUEST} is returned.
     *
     * @param requestPurchaseDto {@code RequestPurchaseDto} with values for {@code Purchase} to create.
     * @return created purchase.
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResponsePurchaseDto> createPurchase(@Valid @RequestBody RequestPurchaseDto requestPurchaseDto) {
        ResponsePurchaseDto createdPurchase = purchaseService.createPurchase(requestPurchaseDto);
        return new ResponseEntity<>(responsePurchaseDtoLinkBuilder.buildLinks(createdPurchase),
                HttpStatus.CREATED);
    }


}
