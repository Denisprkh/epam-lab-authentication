package com.epam.esm.service.impl;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.dto.*;
import com.epam.esm.dto.mapper.impl.ResponseOrderDtoMapper;
import com.epam.esm.entity.Order;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class OrderServiceTest {

    private static final Integer DEFAULT_ID = 1;

    @Mock
    private OrderDao orderDao;

    @Mock
    private PaginationContextBuilder paginationContextBuilder;

    @Mock
    private ResponseOrderDtoMapper responseOrderDtoMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void findOrderByIdTest() {
        Order foundDaoOrder = new Order();
        foundDaoOrder.setId(DEFAULT_ID);
        ResponseOrderDto expectedOrderDto = new ResponseOrderDto();
        expectedOrderDto.setId(DEFAULT_ID);

        when(orderDao.findById(DEFAULT_ID)).thenReturn(foundDaoOrder);
        when(responseOrderDtoMapper.toDto(any(Order.class))).thenReturn(expectedOrderDto);
        ResponseOrderDto resultOrderDto = orderService.findOrderById(DEFAULT_ID);

        assertEquals(expectedOrderDto, resultOrderDto);
    }

    @Test
    void findOrderByIdTestShouldThrowException() {
        when(orderDao.findById(DEFAULT_ID)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> orderService.findOrderById(DEFAULT_ID));
    }

    @Test
    void findAllOrdersTest() {
        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        Order anotherOrder = new Order();
        orders.add(order);
        orders.add(anotherOrder);
        List<ResponseOrderDto> expectedOrders = new ArrayList<>();
        expectedOrders.add(new ResponseOrderDto());
        expectedOrders.add(new ResponseOrderDto());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "2");
        Pagination pagination = buildPagination();

        when(orderDao.findAll(0, 2)).thenReturn(orders);
        when(responseOrderDtoMapper.toDto(any(Order.class))).thenReturn(new ResponseOrderDto());
        when(paginationContextBuilder.defineRecordsPerPageAmount(2)).thenReturn(2);
        when(paginationContextBuilder.defineStartOfRecords(pagination)).thenReturn(0);
        int expectedOrdersSize = expectedOrders.size();
        int resultOrdersSize = orderService.findAllOrders(pagination).size();

        assertEquals(expectedOrdersSize, resultOrdersSize);
    }

    @Test
    void findOrdersByUserIdTest() {
        int userId = 1;
        List<Order> usersOrders = new ArrayList<>();
        Order order = new Order();
        usersOrders.add(order);
        List<ResponseOrderDto> expectedUsersOrdersDto = new ArrayList<>();
        ResponseOrderDto responseOrderDto = new ResponseOrderDto();
        expectedUsersOrdersDto.add(responseOrderDto);
        int expectedOrdersSize = expectedUsersOrdersDto.size();

        when(responseOrderDtoMapper.toDto(any(Order.class))).thenReturn(new ResponseOrderDto());
        when(orderDao.findOrdersByUserId(userId)).thenReturn(usersOrders);
        int resultOrdersSize = orderService.findOrdersByUserId(userId).size();

        assertEquals(expectedOrdersSize, resultOrdersSize);
    }

    private Pagination buildPagination() {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(2);
        return pagination;
    }
}
