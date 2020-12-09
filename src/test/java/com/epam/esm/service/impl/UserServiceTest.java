package com.epam.esm.service.impl;

import com.epam.esm.dao.user.UserDao;
import com.epam.esm.dao.user.UserRoleDao;
import com.epam.esm.dto.RequestUserDto;
import com.epam.esm.dto.ResponseUserDto;
import com.epam.esm.dto.mapper.impl.RequestUserDtoMapper;
import com.epam.esm.dto.mapper.impl.ResponseUserDtoMapper;
import com.epam.esm.entity.Pagination;
import com.epam.esm.entity.User;
import com.epam.esm.entity.UserRole;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Integer DEFAULT_ID = 1;

    @Mock
    private UserDao userDao;

    @Mock
    private PaginationContextBuilder paginationContextBuilder;

    @Mock
    private ResponseUserDtoMapper responseUserDtoMapper;

    @Mock
    private UserRoleDao userRoleDao;

    @Mock
    private RequestUserDtoMapper requestUserDtoMapper;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findUserByIdTest() {
        User user = new User();
        user.setId(DEFAULT_ID);
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(DEFAULT_ID);

        when(userDao.findById(DEFAULT_ID)).thenReturn(user);
        when(responseUserDtoMapper.toDto(user)).thenReturn(responseUserDto);
        ResponseUserDto expectedUser = new ResponseUserDto();
        expectedUser.setId(DEFAULT_ID);
        ResponseUserDto resultUser = userService.findUserById(DEFAULT_ID);

        assertEquals(expectedUser, resultUser);

    }

    @Test
    void findUserByIdShouldThrowException() {
        when(userDao.findById(DEFAULT_ID)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(DEFAULT_ID));
    }

    @Test
    void findAllUsersTest() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("size", "2");
        Pagination pagination = buildPagination();

        when(userDao.findAll(0, 2)).thenReturn(users);
        when(responseUserDtoMapper.toDto(any())).thenReturn(new ResponseUserDto());
        when(paginationContextBuilder.defineRecordsPerPageAmount(2)).thenReturn(2);
        when(paginationContextBuilder.defineStartOfRecords(pagination)).thenReturn(0);
        int expectedUsersSize = users.size();
        int resultUsersSize = userService.findAllUsers(pagination).size();

        assertEquals(expectedUsersSize, resultUsersSize);
    }

    @Test
    void createUserTest(){
        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setLogin("login");
        requestUserDto.setPassword("password");
        User mappedToModelUser = buildUser("login", "password");
        User expectedUser = buildUser("login", passwordEncoder.encode("password"));
        expectedUser.setId(1);
        ResponseUserDto expectedResponseDto = buildResponseUserDto("login", 1);

        when(requestUserDtoMapper.toModel(requestUserDto)).thenReturn(mappedToModelUser);
        when(userRoleDao.findByName("ROLE_USER")).thenReturn(buildRoleUser());
        when(userDao.create(mappedToModelUser)).thenReturn(expectedUser);
        when(responseUserDtoMapper.toDto(expectedUser)).thenReturn(expectedResponseDto);
        ResponseUserDto resultResponseUserDto = userService.createUser(requestUserDto);

        assertEquals(expectedResponseDto, resultResponseUserDto);

    }

    private Pagination buildPagination(){
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(2);
        return pagination;
    }

    private UserRole buildRoleUser(){
        UserRole userRole = new UserRole();
        userRole.setId(1);
        userRole.setName("ROLE_USER");
        return userRole;
    }

    private User buildUser(String login, String password){
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        return user;
    }

    private ResponseUserDto buildResponseUserDto(String login, int id){
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(id);
        responseUserDto.setLogin(login);
        return responseUserDto;
    }
}
