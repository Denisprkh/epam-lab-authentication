package com.epam.esm.dao.tag.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.dao.tag.TagParameter;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import com.epam.esm.util.ResourceBundleErrorMessage;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import static java.util.Objects.nonNull;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

    private final EntityManager entityManager;
    private static final int SUCCESSFULLY_UPDATED_ROW = 1;
    private static final String FIND_THE_MOST_POPULAR_TAG_IN_HIGHEST_SUM_OF_PURCHASES = "SELECT tag_id, name FROM " +
            "(SELECT tag.tag_id AS tag_id, tag.name AS name, COUNT(tag.tag_id) AS tag_count FROM tag JOIN " +
            "gift_certificate_tag ON tag.tag_id = gift_certificate_tag.tag_id JOIN gift_certificate ON " +
            "gift_certificate_tag.gift_certificate_id = gift_certificate.gift_certificate_id JOIN purchase_gift_certificate " +
            "ON gift_certificate.gift_certificate_id = purchase_gift_certificate.gift_certificate_id JOIN purchase " +
            "ON purchase_gift_certificate.purchase_id = purchase.purchase_id JOIN (select SUM(purchase.cost) AS" +
            " purchase_cost, purchase.user_id AS user_id FROM purchase GROUP BY user_id) AS ho ON " +
            "purchase.user_id = ho.user_id WHERE purchase_cost = (SELECT SUM(cost) AS purchase_cost FROM purchase " +
            "GROUP BY purchase.user_id ORDER BY purchase_cost DESC LIMIT 1) GROUP BY tag_id ORDER BY tag_count DESC limit 1) AS res";
    private static final String DELETE_GIFT_CERTIFICATE_TAG = "DELETE FROM gift_certificate_tag WHERE tag_id=:tag_id";
    private static final String FIND_TAG_BY_NAME = "FROM Tag t where t.name=:name";
    private static final String FIND_TAGS = "FROM Tag";

    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Tag create(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Tag findById(Integer id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (nonNull(tag)) {
            return tag;
        }
        throw new ResourceNotFoundException(ResourceBundleErrorMessage.RESOURCE_NOT_FOUND, id);
    }

    @Override
    public boolean delete(Integer id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (nonNull(tag)) {
            entityManager.remove(tag);
            return true;
        }
        throw new ResourceNotFoundException(ResourceBundleErrorMessage.RESOURCE_NOT_FOUND, id);
    }

    @Override
    public Tag update(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tag> findAll(int startOfRecords, int recordsPerPageAmount) {
        return entityManager.createQuery(FIND_TAGS, Tag.class)
                .setFirstResult(startOfRecords)
                .setMaxResults(recordsPerPageAmount)
                .getResultList();
    }

    @Override
    public boolean deleteGiftCertificateTag(Integer id) {
        Query query = entityManager.unwrap(Session.class).createSQLQuery(DELETE_GIFT_CERTIFICATE_TAG);
        query.setParameter(TagParameter.TAG_ID, id);
        return query.executeUpdate() >= SUCCESSFULLY_UPDATED_ROW;
    }

    @Override
    public Optional<Tag> findTagByName(String tagName) {
        try {
            Optional<Tag> foundTag = Optional.ofNullable((Tag) entityManager.createQuery(FIND_TAG_BY_NAME).
                    setParameter(TagParameter.TAG_NAME, tagName).getSingleResult());
            return foundTag;
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Tag findTheMostPopularTagInUserWithTheHighestCostOfPurchases() {
        return entityManager.unwrap(Session.class).createNativeQuery(FIND_THE_MOST_POPULAR_TAG_IN_HIGHEST_SUM_OF_PURCHASES,
                Tag.class).getSingleResult();
    }

    @Override
    public Long findQuantity() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Tag.class)));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
