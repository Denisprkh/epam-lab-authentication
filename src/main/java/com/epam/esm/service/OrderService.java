package com.epam.esm.service;

import com.epam.esm.dto.RequestOrderDto;
import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.entity.Pagination;
import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * {@code Order} service interface.
 */
public interface OrderService {

    /**
     * Creates order.
     *
     * @param requestOrderDto {@code RequestOrderDto} which represents {@code Order} for creation.
     * @return {@code ResponseOrderDto} which represents created {@code Order}
     */
    ResponseOrderDto createOrder(RequestOrderDto requestOrderDto);

    /**
     * Finds order by id.
     *
     * @param id {@code Order}'s id.
     * @return {@code ResponseOrderDto} which represents found {@code Order}
     * @throws ResourceNotFoundException if order with that id doesn't exist.
     */
    ResponseOrderDto findOrderById(Integer id);

    /**
     * Finds all orders corresponding to given page, number of records per page.
     *
     * @param pagination {@code Pagination} which contains page number and records per page amount.
     * @return found orders.
     */
    List<ResponseOrderDto> findAllOrders(Pagination pagination);

    /**
     * Finds users orders.
     *
     * @param userId {@code User}'s id.
     * @return {@code List<ResponseOrderDto} which represents users orders.
     */
    List<ResponseOrderDto> findOrdersByUserId(Integer userId);

    /**
     * Finds quantity of all orders.
     *
     * @return {@code Long} quantity
     */
    Long findQuantity();
}
