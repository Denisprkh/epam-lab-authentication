package com.epam.esm.dao.order.impl;

import com.epam.esm.dao.order.OrderDao;
import com.epam.esm.entity.Order;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.stereotype.Repository;

import static java.util.Objects.nonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final EntityManager entityManager;
    private static final String FIND_ORDERS = "FROM Order";
    private static final String FIND_ORDERS_BY_USER_ID = "FROM Order o WHERE o.user.id=:user_id";
    private static final String USER_ID_PARAMETER = "user_id";

    public OrderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Order create(Order order) {
        entityManager.persist(order);
        return order;
    }

    @Override
    public Order findById(Integer id) {
        Order order = entityManager.find(Order.class, id);
        if (nonNull(order)) {
            return order;
        }
        throw new ResourceNotFoundException(ResourceBundleErrorMessage.RESOURCE_NOT_FOUND, id);
    }

    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Order update(Order order) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> findAll(int startOfRecords, int recordsPerPageAmount) {
        return entityManager.createQuery(FIND_ORDERS, Order.class)
                .setFirstResult(startOfRecords)
                .setMaxResults(recordsPerPageAmount)
                .getResultList();
    }

    @Override
    public List<Order> findOrdersByUserId(Integer userId) {
        return entityManager.createQuery(FIND_ORDERS_BY_USER_ID, Order.class)
                .setParameter(USER_ID_PARAMETER, userId).getResultList();
    }

    @Override
    public Long findQuantity() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Order.class)));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
