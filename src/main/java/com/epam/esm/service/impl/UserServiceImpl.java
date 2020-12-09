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
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.service.UserService;
import com.epam.esm.service.util.pagination.PaginationContextBuilder;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PaginationContextBuilder paginationContextBuilder;
    private final ResponseUserDtoMapper responseUserDtoMapper;
    private final RequestUserDtoMapper requestUserDtoMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRoleDao userRoleDao;
    private static final String ROLE_USER = "ROLE_USER";

    public UserServiceImpl(UserDao userDao, PaginationContextBuilder paginationContextBuilder,
                           ResponseUserDtoMapper responseUserDtoMapper, RequestUserDtoMapper requestUserDtoMapper,
                           BCryptPasswordEncoder passwordEncoder, UserRoleDao userRoleDao) {
        this.userDao = userDao;
        this.paginationContextBuilder = paginationContextBuilder;
        this.responseUserDtoMapper = responseUserDtoMapper;
        this.requestUserDtoMapper = requestUserDtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleDao = userRoleDao;
    }

    @Override
    public ResponseUserDto findUserById(Integer id) {
        return responseUserDtoMapper.toDto(userDao.findById(id));
    }

    @Override
    public List<ResponseUserDto> findAllUsers(Pagination pagination) {
        int startOfRecords = paginationContextBuilder.defineStartOfRecords(pagination);
        int recordsPerPageAmount = paginationContextBuilder.defineRecordsPerPageAmount(pagination.getSize());
        return userDao.findAll(startOfRecords, recordsPerPageAmount).stream().map(responseUserDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long findQuantity() {
        return userDao.findQuantity();
    }

    @Override
    public Optional<User> findUserByLogin(String login) {
        return userDao.findByLogin(login);
    }

    @Override
    @Transactional
    public ResponseUserDto createUser(RequestUserDto requestUserDto) {
        if (findUserByLogin(requestUserDto.getLogin()).isPresent()) {
            throw new ResourceAlreadyExistsException(ResourceBundleErrorMessage.USER_ALREADY_EXISTS);
        }
        UserRole userRole = userRoleDao.findByName(ROLE_USER);
        User userForCreation = requestUserDtoMapper.toModel(requestUserDto);
        userForCreation.setPassword(passwordEncoder.encode(userForCreation.getPassword()));
        userForCreation.setUserRole(userRole);
        return responseUserDtoMapper.toDto(userDao.create(userForCreation));
    }
}
