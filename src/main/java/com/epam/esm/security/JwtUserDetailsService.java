package com.epam.esm.security;

import com.epam.esm.dao.user.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.security.JwtUser;
import com.epam.esm.security.mapper.UserToUserDetailsMapper;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserDao userDao;
    private final UserToUserDetailsMapper<JwtUser, User> jwtUserMapper;

    public JwtUserDetailsService(UserDao userDao, UserToUserDetailsMapper<JwtUser, User> jwtUserMapper) {
        this.userDao = userDao;
        this.jwtUserMapper = jwtUserMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        Optional<User> foundUser = userDao.findByLogin(login);
        if (foundUser.isPresent()) {
            return jwtUserMapper.toUserDetails(foundUser.get());
        }
        throw new UsernameNotFoundException(ResourceBundleErrorMessage.USER_NOT_FOUND);
    }
}
