package com.epam.esm.dao.user;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.entity.User;

import java.util.Optional;

/**
 * {@code User} dao interface.
 */
public interface UserDao extends CommonDao<User, Integer> {

    /**
     * Finds amount of all {@code User} stored in datasource
     *
     * @return {@code Long} amount.
     */
    Long findQuantity();

    /**
     * Find user by login.
     *
     * @param login {@code User}'s login.
     * @return {@code Optional<User>}.
     */
    Optional<User> findByLogin(String login);

}
