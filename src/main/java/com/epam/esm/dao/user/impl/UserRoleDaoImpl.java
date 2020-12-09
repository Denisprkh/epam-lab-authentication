package com.epam.esm.dao.user.impl;

import com.epam.esm.dao.user.UserRoleDao;
import com.epam.esm.entity.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {

    private final EntityManager entityManager;
    private static final String FIND_USER_ROLE_BY_NAME = "FROM UserRole WHERE name=:name";
    private static final String PARAMETER_USER_ROLE_NAME = "name";

    public UserRoleDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserRole findByName(String name) {
        return entityManager.createQuery(FIND_USER_ROLE_BY_NAME, UserRole.class)
                .setParameter(PARAMETER_USER_ROLE_NAME, name)
                .getSingleResult();
    }

}
