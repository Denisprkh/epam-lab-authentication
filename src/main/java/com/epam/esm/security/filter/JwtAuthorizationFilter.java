package com.epam.esm.security.filter;

import com.epam.esm.exception.JwtFormatException;
import com.epam.esm.security.JwtConfig;
import com.epam.esm.security.JwtUserDetailsService;
import com.epam.esm.security.util.JwtUtil;
import com.epam.esm.util.ResourceBundleErrorMessage;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtConfig jwtConfig, JwtUtil jwtUtil, JwtUserDetailsService userDetailsService) {
        this.jwtConfig = jwtConfig;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = null;
        String login = null;

        if (nonNull(authorizationHeader) && authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            try {
                token = jwtUtil.extractToken(authorizationHeader);
                login = jwtUtil.extractLogin(token);
            }catch (JwtException e){
                throw new JwtFormatException(ResourceBundleErrorMessage.JWT_FORMAT);
            }

        }

        if (nonNull(login) && isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails jwtUser = userDetailsService.loadUserByUsername(login);
            if (jwtUtil.tokenIsValid(token, jwtUser)) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        jwtUser.getUsername(),
                        jwtUser.getPassword(),
                        jwtUser.getAuthorities()
                ));
            }
        }
        filterChain.doFilter(request, response);
    }

}
