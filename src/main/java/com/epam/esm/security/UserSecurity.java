package com.epam.esm.security;

import com.epam.esm.dto.ResponseOrderDto;
import com.epam.esm.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

    private final JwtUserDetailsService userDetailsService;
    private final OrderService orderService;

    public UserSecurity(JwtUserDetailsService userDetailsService, OrderService orderService) {
        this.userDetailsService = userDetailsService;
        this.orderService = orderService;
    }

    public boolean hasUserId(Authentication authentication, Integer userId) {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authentication.getName());
        return jwtUser.getId().equals(userId);
    }

    public boolean isUsersOrder(Authentication authentication, Integer orderId) {
        ResponseOrderDto order = orderService.findOrderById(orderId);
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authentication.getName());
        return jwtUser.getId().equals(order.getUserId());
    }
}
