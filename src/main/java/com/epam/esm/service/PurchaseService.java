package com.epam.esm.service;

import com.epam.esm.dto.RequestPurchaseDto;
import com.epam.esm.dto.ResponsePurchaseDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;

/**
 * {@code Purchase} service interface.
 */
public interface PurchaseService {

    /**
     * Creates purchase.
     *
     * @param requestPurchaseDto {@code RequestPurchaseDto} which represents {@code Purchase} for creation.
     * @return {@code ResponsePurchaseDto} which represents created {@code Purchase}
     */
    ResponsePurchaseDto createPurchase(RequestPurchaseDto requestPurchaseDto);

    /**
     * Finds purchase by id.
     *
     * @param id {@code Purchase}'s id.
     * @return {@code ResponsePurchaseDto} which represents found {@code Purchase}
     * @throws ResourceNotFoundException if purchase with that id doesn't exist.
     */
    ResponsePurchaseDto findPurchaseById(Integer id);

    /**
     * Finds all purchases corresponding to given page, number of records per page.
     *
     * @param pagination {@code Pagination} which contains page number and records per page amount.
     * @return found purchases.
     */
    List<ResponsePurchaseDto> findAllPurchases(Pagination pagination);

    /**
     * Finds users purchases.
     *
     * @param userId {@code User}'s id.
     * @return {@code List<ResponsePurchaseDto} which represents users purchases.
     */
    List<ResponsePurchaseDto> findPurchasesByUserId(Integer userId);

    /**
     * Finds quantity of all purchases.
     *
     * @return {@code Long} quantity
     */
    Long findQuantity();
}
