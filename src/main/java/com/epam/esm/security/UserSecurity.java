package com.epam.esm.security;

import com.epam.esm.dto.ResponsePurchaseDto;
import com.epam.esm.service.PurchaseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserSecurity {

    private final JwtUserDetailsService userDetailsService;
    private final PurchaseService purchaseService;

    public UserSecurity(JwtUserDetailsService userDetailsService, PurchaseService purchaseService) {
        this.userDetailsService = userDetailsService;
        this.purchaseService = purchaseService;
    }

    public boolean hasUserId(Authentication authentication, Integer userId) {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authentication.getName());
        return jwtUser.getId().equals(userId);
    }

    public boolean isUsersPurchase(Authentication authentication, Integer purchaseId) {
        ResponsePurchaseDto purchase = purchaseService.findPurchaseById(purchaseId);
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(authentication.getName());
        return jwtUser.getId().equals(purchase.getUserId());
    }
}
