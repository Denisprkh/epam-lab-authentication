package com.epam.esm.dao.impl;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    @Transactional
    void createTest() {
        Order orderForCreation = new Order();
        orderForCreation.setUser(new User(1));
        orderForCreation.setCost(new BigDecimal("150"));
        orderForCreation.setPurchaseDate(LocalDateTime.now());
        orderForCreation.setGiftCertificates(new ArrayList<>(Collections.singleton(new GiftCertificate(1))));
        int expectedId = 3;

        int resultId = orderDao.create(orderForCreation).getId();

        assertEquals(expectedId, resultId);
    }

    @Test
    void findQuantityTest() {
        long expectedQuantity = 2;

        long resultQuantity = orderDao.findQuantity();

        assertEquals(expectedQuantity, resultQuantity);
    }

    @Test
    void findOrdersByUserIdTest() {
        int expectedOrderListSize = 1;

        int resultOrderListSize = orderDao.findOrdersByUserId(1).size();

        assertEquals(expectedOrderListSize, resultOrderListSize);
    }

    @Test
    void findOrdersByUserIdTestShouldReturnEmptyList() {
        int expectedOrderListSize = 0;

        int resultOrderListSize = orderDao.findOrdersByUserId(5).size();

        assertEquals(expectedOrderListSize, resultOrderListSize);
    }

    @Test
    void findByIdTest() {
        int expectedId = 1;

        int resultId = orderDao.findById(1).getId();

        assertEquals(expectedId, resultId);
    }

    @Test
    void findByIdShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> orderDao.findById(5));
    }
}
