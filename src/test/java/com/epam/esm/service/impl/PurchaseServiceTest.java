package com.epam.esm.service.impl;

import com.epam.esm.dao.purchase.PurchaseDao;
import com.epam.esm.dto.*;
import com.epam.esm.dto.mapper.impl.ResponsePurchaseDtoMapper;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.Pagination;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class PurchaseServiceTest {

    private static final Integer DEFAULT_ID = 1;

    @Mock
    private PurchaseDao purchaseDao;

    @Mock
    private PaginationContextBuilder paginationContextBuilder;

    @Mock
    private ResponsePurchaseDtoMapper responsePurchaseDtoMapper;

    @InjectMocks
    private PurchaseServiceImpl purchaseService;

    @Test
    void findPurchaseByIdTest() {
        Purchase foundDaoPurchase = new Purchase();
        foundDaoPurchase.setId(DEFAULT_ID);
        ResponsePurchaseDto expectedPurchaseDto = new ResponsePurchaseDto();
        expectedPurchaseDto.setId(DEFAULT_ID);

        when(purchaseDao.findById(DEFAULT_ID)).thenReturn(foundDaoPurchase);
        when(responsePurchaseDtoMapper.toDto(any(Purchase.class))).thenReturn(expectedPurchaseDto);
        ResponsePurchaseDto resultPurchaseDto = purchaseService.findPurchaseById(DEFAULT_ID);

        assertEquals(expectedPurchaseDto, resultPurchaseDto);
    }

    @Test
    void findPurchaseByIdTestShouldThrowException() {
        when(purchaseDao.findById(DEFAULT_ID)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> purchaseService.findPurchaseById(DEFAULT_ID));
    }

    @Test
    void findAllPurchasesTest() {
        List<Purchase> purchases = new ArrayList<>();
        Purchase purchase = new Purchase();
        Purchase anotherPurchase = new Purchase();
        purchases.add(purchase);
        purchases.add(anotherPurchase);
        List<ResponsePurchaseDto> expectedPurchases = new ArrayList<>();
        expectedPurchases.add(new ResponsePurchaseDto());
        expectedPurchases.add(new ResponsePurchaseDto());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "2");
        Pagination pagination = buildPagination();

        when(purchaseDao.findAll(0, 2)).thenReturn(purchases);
        when(responsePurchaseDtoMapper.toDto(any(Purchase.class))).thenReturn(new ResponsePurchaseDto());
        when(paginationContextBuilder.defineRecordsPerPageAmount(2)).thenReturn(2);
        when(paginationContextBuilder.defineStartOfRecords(pagination)).thenReturn(0);
        int expectedPurchasesSize = expectedPurchases.size();
        int resultPurchasesSize = purchaseService.findAllPurchases(pagination).size();

        assertEquals(expectedPurchasesSize, resultPurchasesSize);
    }

    @Test
    void findPurchasesByUserIdTest() {
        int userId = 1;
        List<Purchase> usersPurchases = new ArrayList<>();
        Purchase purchase = new Purchase();
        usersPurchases.add(purchase);
        List<ResponsePurchaseDto> expectedUsersPurchasesDto = new ArrayList<>();
        ResponsePurchaseDto responsePurchaseDto = new ResponsePurchaseDto();
        expectedUsersPurchasesDto.add(responsePurchaseDto);
        int expectedPurchasesSize = expectedUsersPurchasesDto.size();

        when(responsePurchaseDtoMapper.toDto(any(Purchase.class))).thenReturn(new ResponsePurchaseDto());
        when(purchaseDao.findPurchasesByUserId(userId)).thenReturn(usersPurchases);
        int resultPurchasesSize = purchaseService.findPurchasesByUserId(userId).size();

        assertEquals(expectedPurchasesSize, resultPurchasesSize);
    }

    private Pagination buildPagination() {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(2);
        return pagination;
    }
}
