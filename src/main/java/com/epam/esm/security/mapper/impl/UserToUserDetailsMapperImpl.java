package com.epam.esm.security.mapper.impl;

import com.epam.esm.entity.User;
import com.epam.esm.security.JwtUser;
import com.epam.esm.security.mapper.UserToUserDetailsMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserToUserDetailsMapperImpl implements UserToUserDetailsMapper<JwtUser, User> {

    @Override
    public JwtUser toUserDetails(User user) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setId(user.getId());
        jwtUser.setLogin(user.getLogin());
        jwtUser.setPassword(user.getPassword());
        jwtUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(user.getUserRole().getName())));
        return jwtUser;
    }
}
