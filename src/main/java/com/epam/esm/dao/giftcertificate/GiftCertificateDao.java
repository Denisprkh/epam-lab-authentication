package com.epam.esm.dao.giftcertificate;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateCriteria;

import java.util.List;
import java.util.Optional;

/**
 * {@code GiftCertificate} dao interface.
 */
public interface GiftCertificateDao extends CommonDao<GiftCertificate, Integer> {

    /**
     * Finds gift certificate by name in datasource.
     *
     * @param certificateName Gift certificates name.
     * @return {@code Optional<GiftCertificate>} found gift certificate.
     */
    Optional<GiftCertificate> findGiftCertificateByName(String certificateName);


    /**
     * Finds quantity of all certificates according to criteria.
     *
     * @param giftCertificateCriteria criteria.
     * @return found quantity.
     */
    Integer findQuantity(GiftCertificateCriteria giftCertificateCriteria);

    /**
     * Finds all gift certificates corresponding to given page, number of records per page and sorting/searching parameters.
     *
     * @param giftCertificateCriteria {@code GiftCertificateCriteria} that contains values by which to sort and search.
     * @param startOfRecords          start of records.
     * @param recordsPerPageAmount    records per page amount.
     * @return found certificates.
     */
    List<GiftCertificate> findAll(GiftCertificateCriteria giftCertificateCriteria, int startOfRecords,
                                  int recordsPerPageAmount);

}
