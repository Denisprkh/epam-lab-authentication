package com.epam.esm.dao.purchase;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.entity.Purchase;

import java.util.List;

/**
 * {@code Purchase} dao interface.
 */
public interface PurchaseDao extends CommonDao<Purchase, Integer> {

    /**
     * Finds purchases by users id
     *
     * @param userId users id
     * @return {@code List<Purchase>} found purchases
     */
    List<Purchase> findPurchasesByUserId(Integer userId);

    /**
     * Finds amount of all {@code Purchase} stored in datasource
     *
     * @return {@code Long} amount.
     */
    Long findQuantity();

}
