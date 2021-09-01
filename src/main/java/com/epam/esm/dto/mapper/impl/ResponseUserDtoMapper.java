package com.epam.esm.dto.mapper.impl;

import com.epam.esm.dto.ResponseUserDto;
import com.epam.esm.dto.mapper.DtoMapper;
import com.epam.esm.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ResponseUserDtoMapper implements DtoMapper<ResponseUserDto, User> {

    @Override
    public ResponseUserDto toDto(User user) {
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setId(user.getId());
        responseUserDto.setLogin(user.getLogin());
        return responseUserDto;
    }

    @Override
    public User toModel(ResponseUserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setLogin(userDto.getLogin());
        return user;
    }
}
