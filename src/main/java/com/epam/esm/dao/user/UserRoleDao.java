package com.epam.esm.dao.user;

import com.epam.esm.entity.UserRole;

public interface UserRoleDao {

    /**
     * Finds users role with default flag equals {@code True}.
     *
     * @return found role.
     */
    UserRole findDefault();

}
