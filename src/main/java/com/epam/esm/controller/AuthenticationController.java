package com.epam.esm.controller;

import com.epam.esm.dto.RequestUserDto;
import com.epam.esm.dto.ResponseUserDto;
import com.epam.esm.entity.AuthenticationResponse;
import com.epam.esm.security.JwtUser;
import com.epam.esm.security.util.JwtUtil;
import com.epam.esm.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthenticationController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Creates user in database.
     *
     * @param requestUserDto {@code RequestUserDto} with login and password.
     * @return {@code ResponseUserDto} which represents created user.
     */
    @PostMapping(value = "/signUp")
    @PreAuthorize("permitAll()")
    public ResponseUserDto signUp(@Valid @RequestBody RequestUserDto requestUserDto) {
        return userService.createUser(requestUserDto);
    }

    /**
     * Authorizes user.
     *
     * @param requestUserDto {@code RequestUserDto} with login and password.
     * @return {@code AuthenticationResponse} with jwt.
     */
    @PostMapping(value = "/signIn")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AuthenticationResponse> signIn(@Valid @RequestBody RequestUserDto requestUserDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestUserDto.getLogin(), requestUserDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtUser userDetails = (JwtUser) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
    }
}
