package com.epam.esm.dao.user;

import com.epam.esm.entity.UserRole;

public interface UserRoleDao {

    /**
     * Finds users role by name.
     *
     * @param name {@code UserRole}'s name.
     * @return found role.
     */
    UserRole findByName(String name);

}
