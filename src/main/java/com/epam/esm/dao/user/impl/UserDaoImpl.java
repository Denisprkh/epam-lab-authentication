package com.epam.esm.dao.user.impl;

import com.epam.esm.dao.user.UserDao;
import com.epam.esm.entity.User;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.stereotype.Repository;

import static java.util.Objects.nonNull;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;
    private static final String FIND_USERS = "FROM User";
    private static final String PARAMETER_LOGIN = "login";
    private static final String FIND_USER_BY_LOGIN = "FROM User u WHERE u.login=:login";

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public User findById(Integer id) {
        User user = entityManager.find(User.class, id);
        if (nonNull(user)) {
            return user;
        }
        throw new ResourceNotFoundException(ResourceBundleErrorMessage.RESOURCE_NOT_FOUND, id);
    }

    @Override
    public boolean delete(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User update(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> findAll(int startOfRecords, int recordsPerPageAmount) {
        return entityManager.createQuery(FIND_USERS, User.class)
                .setFirstResult(startOfRecords)
                .setMaxResults(recordsPerPageAmount)
                .getResultList();
    }

    @Override
    public Long findQuantity() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class)));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try {
            Optional<User> foundUser = Optional.of(entityManager.createQuery(FIND_USER_BY_LOGIN, User.class)
                    .setParameter(PARAMETER_LOGIN, login)
                    .getSingleResult());
            return foundUser;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
