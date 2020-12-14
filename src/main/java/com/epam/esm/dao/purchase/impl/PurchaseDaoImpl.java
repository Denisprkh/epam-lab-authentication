package com.epam.esm.dao.purchase.impl;

import com.epam.esm.dao.purchase.PurchaseDao;
import com.epam.esm.entity.Purchase;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.stereotype.Repository;

import static java.util.Objects.nonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Repository
public class PurchaseDaoImpl implements PurchaseDao {

    private final EntityManager entityManager;
    private static final String FIND_PURCHASES = "FROM Purchase";
    private static final String FIND_PURCHASES_BY_USER_ID = "FROM Purchase o WHERE o.user.id=:user_id";
    private static final String USER_ID_PARAMETER = "user_id";

    public PurchaseDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Purchase create(Purchase purchase) {
        entityManager.persist(purchase);
        return purchase;
    }

    @Override
    public Purchase findById(Integer id) {
        Purchase purchase = entityManager.find(Purchase.class, id);
        if (nonNull(purchase)) {
            return purchase;
        }
        throw new ResourceNotFoundException(ResourceBundleErrorMessage.RESOURCE_NOT_FOUND, id);
    }

    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Purchase update(Purchase purchase) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Purchase> findAll(int startOfRecords, int recordsPerPageAmount) {
        return entityManager.createQuery(FIND_PURCHASES, Purchase.class)
                .setFirstResult(startOfRecords)
                .setMaxResults(recordsPerPageAmount)
                .getResultList();
    }

    @Override
    public List<Purchase> findPurchasesByUserId(Integer userId) {
        return entityManager.createQuery(FIND_PURCHASES_BY_USER_ID, Purchase.class)
                .setParameter(USER_ID_PARAMETER, userId).getResultList();
    }

    @Override
    public Long findQuantity() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Purchase.class)));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
