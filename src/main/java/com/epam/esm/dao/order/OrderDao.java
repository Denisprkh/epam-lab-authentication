package com.epam.esm.dao.order;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.entity.Order;

import java.util.List;

/**
 * {@code Order} dao interface.
 */
public interface OrderDao extends CommonDao<Order, Integer> {

    /**
     * Finds orders by users id
     *
     * @param userId users id
     * @return {@code List<Order>} found orders
     */
    List<Order> findOrdersByUserId(Integer userId);

    /**
     * Finds amount of all {@code Order} stored in datasource
     *
     * @return {@code Long} amount.
     */
    Long findQuantity();

}
