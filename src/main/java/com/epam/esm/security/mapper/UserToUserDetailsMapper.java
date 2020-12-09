package com.epam.esm.security.mapper;

public interface UserToUserDetailsMapper<T, K> {

    T toUserDetails(K k);

}
