package com.epam.esm.dao.giftcertificate.impl;

import com.epam.esm.dao.util.GiftCertificateSearchCriteriaBuilder;
import com.epam.esm.dao.giftcertificate.GiftCertificateDao;
import com.epam.esm.dao.giftcertificate.GiftCertificateParameter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.dao.util.GiftCertificateCriteria;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.springframework.stereotype.Repository;

import static java.util.Objects.nonNull;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final EntityManager entityManager;
    private final GiftCertificateSearchCriteriaBuilder searchCriteriaBuilder;
    public static final String FIND_GIFT_CERTIFICATE_BY_NAME = "FROM GiftCertificate gc WHERE gc.name=:name";
    private static final String GIFT_CERTIFICATE_ALIAS = "certificateAlias";
    private static final String JOIN_TAG_ALIAS = "tagAlias";
    private static final String ASSOCIATION_LIST_NAME = "tags";

    public GiftCertificateDaoImpl(EntityManager entityManager, GiftCertificateSearchCriteriaBuilder searchCriteriaBuilder) {
        this.entityManager = entityManager;
        this.searchCriteriaBuilder = searchCriteriaBuilder;
    }

    @Override
    public GiftCertificate create(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        return giftCertificate;
    }

    @Override
    public GiftCertificate findById(Integer id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (nonNull(giftCertificate)) {
            return giftCertificate;
        }
        throw new ResourceNotFoundException(ResourceBundleErrorMessage.RESOURCE_NOT_FOUND, id);
    }

    @Override
    public boolean delete(Integer id) {
        GiftCertificate giftCertificate = entityManager.find(GiftCertificate.class, id);
        if (nonNull(giftCertificate)) {
            entityManager.remove(giftCertificate);
            return true;
        }
        throw new ResourceNotFoundException(ResourceBundleErrorMessage.RESOURCE_NOT_FOUND, id);
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        entityManager.merge(giftCertificate);
        return giftCertificate;
    }

    @Override
    public List<GiftCertificate> findAll(int startOfRecords, int recordsPerPageAmount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<GiftCertificate> findGiftCertificateByName(String certificateName) {
        try {
            Optional<GiftCertificate> foundGiftCertificate = Optional.ofNullable((GiftCertificate) entityManager.
                    createQuery(FIND_GIFT_CERTIFICATE_BY_NAME).
                    setParameter(GiftCertificateParameter.GIFT_CERTIFICATE_NAME, certificateName).getSingleResult());
            return foundGiftCertificate;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Integer findQuantity(GiftCertificateCriteria giftCertificateCriteria) {
        CriteriaQuery<GiftCertificate> searchQuery = searchCriteriaBuilder.buildCriteriaQuery(giftCertificateCriteria,
                entityManager, GIFT_CERTIFICATE_ALIAS, JOIN_TAG_ALIAS);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> giftCertificateRoot = criteriaQuery.from(GiftCertificate.class);
        giftCertificateRoot.alias(GIFT_CERTIFICATE_ALIAS);
        giftCertificateRoot.join(ASSOCIATION_LIST_NAME).alias(JOIN_TAG_ALIAS);
        criteriaQuery.where(searchQuery.getRestriction());
        criteriaQuery.select(criteriaBuilder.countDistinct(giftCertificateRoot));
        return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
    }

    @Override
    public List<GiftCertificate> findAll(GiftCertificateCriteria giftCertificateCriteria, int startOfRecords,
                                         int recordsPerPageAmount) {
        CriteriaQuery<GiftCertificate> searchQuery = searchCriteriaBuilder.buildCriteriaQuery(giftCertificateCriteria,
                entityManager, GIFT_CERTIFICATE_ALIAS, JOIN_TAG_ALIAS);
        return entityManager.createQuery(searchQuery)
                .setFirstResult(startOfRecords)
                .setMaxResults(recordsPerPageAmount)
                .getResultList();
    }
}
