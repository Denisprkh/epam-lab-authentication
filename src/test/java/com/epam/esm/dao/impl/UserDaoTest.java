package com.epam.esm.dao.impl;

import com.epam.esm.dao.user.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    @Transactional
    void createTest() {
        User userForCreation = buildUser();
        int expectedId = 3;

        int resultId = userDao.create(userForCreation).getId();

        assertEquals(expectedId, resultId);

    }

    @Test
    void findByIdTest() {
        String expectedLogin = "john@gmail.com";

        String resultLogin = userDao.findById(1).getLogin();

        assertEquals(expectedLogin, resultLogin);
    }

    @Test
    void findQuantityTest() {
        long expectedQuantity = 2;

        long resultQuantity = userDao.findQuantity();

        assertEquals(expectedQuantity, resultQuantity);
    }

    @Test
    void findAll() {
        int expectedUsersListSize = 1;

        int resultUsersListSize = userDao.findAll(0, 1).size();

        assertEquals(expectedUsersListSize, resultUsersListSize);
    }

    private User buildUser() {
        User user = new User();
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setName("ROLE_USER");
        user.setLogin("Login");
        user.setPassword("Password");
        return user;
    }
}
