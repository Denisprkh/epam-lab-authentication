package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.RequestUserDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RequestUserDtoMapper implements DtoMapper<RequestUserDto, User> {

    @Override
    public RequestUserDto toDto(User user) {
        RequestUserDto requestUserDto = new RequestUserDto();
        requestUserDto.setLogin(user.getLogin());
        requestUserDto.setPassword(user.getPassword());
        return requestUserDto;
    }

    @Override
    public User toModel(RequestUserDto requestUserDto) {
        User user = new User();
        user.setLogin(requestUserDto.getLogin());
        user.setPassword(requestUserDto.getPassword());
        return user;
    }
}
