package com.epam.esm.dao.util;

import com.epam.esm.dao.giftcertificate.GiftCertificateParameter;
import com.epam.esm.dao.tag.TagParameter;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateCriteria;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.List;

@Component
public final class GiftCertificateSearchCriteriaBuilder {

    private static final String ASC_ORDER_PARAM = ":asc";
    private static final String DESC_ORDER_PARAM = ":desc";
    private static final String EMPTY_STRING = "";
    private static final String PERCENT_SIGN = "%";
    private static final String ASSOCIATION_LIST_NAME = "tags";


    public CriteriaQuery<GiftCertificate> buildCriteriaQuery(GiftCertificateCriteria giftCertificateCriteria
            , EntityManager entityManager) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        List<Predicate> predicates = new ArrayList<>();
        if (nonNull(giftCertificateCriteria.getGiftCertDesc())) {
            predicates.add(buildDescriptionSearchPredicate(certificateRoot, giftCertificateCriteria, criteriaBuilder));
        }

        if (nonNull(giftCertificateCriteria.getGiftCertName())) {
            predicates.add(buildNameSearchPredicate(certificateRoot, giftCertificateCriteria, criteriaBuilder));
        }

        if (nonNull(giftCertificateCriteria.getTagName())) {
            Expression<Long> countOfCertificatesInGroup = criteriaBuilder.count(certificateRoot);
            int tagParameterSize = giftCertificateCriteria.getTagName().size();
            predicates.add(criteriaBuilder.and(buildTagNameSearchPredicate(certificateRoot, giftCertificateCriteria)));
            criteriaQuery.
                    groupBy(certificateRoot).having(criteriaBuilder.equal(countOfCertificatesInGroup, tagParameterSize));
        }

        if (nonNull(giftCertificateCriteria.getOrder())) {
            criteriaQuery.orderBy(defineCriteriaOrder(certificateRoot, giftCertificateCriteria, criteriaBuilder));
        }
        Predicate andQueryWhereClause = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        criteriaQuery.where(andQueryWhereClause);
        return criteriaQuery;
    }

    private Predicate buildDescriptionSearchPredicate(Root<GiftCertificate> certificateRoot, GiftCertificateCriteria giftCertificateCriteria,
                                                      CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(certificateRoot.get(GiftCertificateParameter.GIFT_CERTIFICATE_DESCRIPTION),
                PERCENT_SIGN + giftCertificateCriteria.getGiftCertDesc() + PERCENT_SIGN);
    }

    private Predicate buildNameSearchPredicate(Root<GiftCertificate> certificateRoot, GiftCertificateCriteria giftCertificateCriteria,
                                               CriteriaBuilder criteriaBuilder) {
        return criteriaBuilder.like(certificateRoot.get(GiftCertificateParameter.GIFT_CERTIFICATE_NAME),
                PERCENT_SIGN + giftCertificateCriteria.getGiftCertName() + PERCENT_SIGN);
    }

    private Predicate buildTagNameSearchPredicate(Root<GiftCertificate> certificateRoot, GiftCertificateCriteria giftCertificateCriteria) {
        List<String> tagNames = new ArrayList<>(giftCertificateCriteria.getTagName());
        Join<GiftCertificate, Tag> tagTable = certificateRoot.join(ASSOCIATION_LIST_NAME);
        return tagTable.get(TagParameter.TAG_NAME).in(tagNames);
    }

    private Order defineCriteriaOrder(Root<GiftCertificate> certificateRoot, GiftCertificateCriteria giftCertificateCriteria,
                                      CriteriaBuilder criteriaBuilder) {
        String orderParam = giftCertificateCriteria.getOrder();
        Order order = null;
        if (orderParam.contains(ASC_ORDER_PARAM)) {
            order = criteriaBuilder.asc(certificateRoot.get(orderParam.replace
                    (ASC_ORDER_PARAM, EMPTY_STRING)));
        } else if (orderParam.contains(DESC_ORDER_PARAM)) {
            order = criteriaBuilder.desc(certificateRoot.get(orderParam.replace
                    (DESC_ORDER_PARAM, EMPTY_STRING)));
        }
        return order;
    }

}
