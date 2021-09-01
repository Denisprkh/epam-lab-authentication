package com.epam.esm.dao.impl;

import com.epam.esm.dao.purchase.PurchaseDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Purchase;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PurchaseDaoTest {

    @Autowired
    private PurchaseDao purchaseDao;

    @Test
    @Transactional
    void createTest() {
        Purchase purchaseForCreation = new Purchase();
        purchaseForCreation.setUser(new User(1));
        purchaseForCreation.setCost(new BigDecimal("150"));
        purchaseForCreation.setPurchaseDate(LocalDateTime.now());
        purchaseForCreation.setGiftCertificates(new ArrayList<>(Collections.singleton(new GiftCertificate(1))));
        int expectedId = 3;

        int resultId = purchaseDao.create(purchaseForCreation).getId();

        assertEquals(expectedId, resultId);
    }

    @Test
    void findQuantityTest() {
        long expectedQuantity = 2;

        long resultQuantity = purchaseDao.findQuantity();

        assertEquals(expectedQuantity, resultQuantity);
    }

    @Test
    void findPurchasesByUserIdTest() {
        int expectedPurchaseListSize = 1;

        int resultPurchaseListSize = purchaseDao.findPurchasesByUserId(1).size();

        assertEquals(expectedPurchaseListSize, resultPurchaseListSize);
    }

    @Test
    void findPurchasesByUserIdTestShouldReturnEmptyList() {
        int expectedPurchaseListSize = 0;

        int resultPurchaseListSize = purchaseDao.findPurchasesByUserId(5).size();

        assertEquals(expectedPurchaseListSize, resultPurchaseListSize);
    }

    @Test
    void findByIdTest() {
        int expectedId = 1;

        int resultId = purchaseDao.findById(1).getId();

        assertEquals(expectedId, resultId);
    }

    @Test
    void findByIdShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> purchaseDao.findById(5));
    }
}
