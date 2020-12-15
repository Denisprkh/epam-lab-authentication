package com.epam.esm.dao.user.impl;

import com.epam.esm.dao.user.UserRoleDao;
import com.epam.esm.entity.UserRole;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRoleDaoImpl implements UserRoleDao {

    private final EntityManager entityManager;
    private static final String PARAMETER_IS_DEFAULT = "isDefault";
    private static final String FIND_USER_ROLE_BY_DEFAULT_FLAG = "FROM UserRole ur WHERE ur.isDefault=:isDefault";

    public UserRoleDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public UserRole findDefault() {
        return entityManager.createQuery(FIND_USER_ROLE_BY_DEFAULT_FLAG, UserRole.class)
                .setParameter(PARAMETER_IS_DEFAULT, Boolean.TRUE)
                .getSingleResult();
    }

}
