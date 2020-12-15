package com.epam.esm.service;

import com.epam.esm.dto.RequestUserDto;
import com.epam.esm.dto.ResponseUserDto;
import com.epam.esm.controller.util.Pagination;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * {@code User} service interface.
 */
public interface UserService {

    /**
     * Finds user by id.
     *
     * @param id {@code User}'s id.
     * @return {@code ResponseUserDto} which represents found user.
     * @throws ResourceNotFoundException if user with that id doesn't exist.
     */
    ResponseUserDto findUserById(Integer id);


    /**
     * Finds all users corresponding to given page, number of records per page.
     *
     * @param pagination {@code Pagination} which contains page number and records per page amount.
     * @return found users.
     */
    List<ResponseUserDto> findAllUsers(Pagination pagination);

    /**
     * Finds quantity of all users.
     *
     * @return {@code Long} quantity
     */
    Long findQuantity();

    /**
     * Finds user by login.
     *
     * @param login {@code User}'s login.
     * @return {@code Optional<User>} found user.
     */
    Optional<User> findUserByLogin(String login);

    /**
     * Creates user.
     *
     * @param requestUserDto {@code RequestUserDto} with login and password values.
     * @return created user.
     * @throws ResourceAlreadyExistsException if a user with the same login already exists.
     */
    ResponseUserDto createUser(RequestUserDto requestUserDto);
}
