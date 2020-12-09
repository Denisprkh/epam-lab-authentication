package com.epam.esm.service;

import com.epam.esm.dto.RequestGiftCertificateDto;
import com.epam.esm.dto.ResponseGiftCertificateDto;
import com.epam.esm.entity.GiftCertificateCriteria;
import com.epam.esm.entity.Pagination;
import com.epam.esm.exception.ResourceAlreadyExistsException;
import com.epam.esm.exception.ResourceNotFoundException;

import java.util.List;

/**
 * {@code GiftCertificate} service interface.
 */
public interface GiftCertificateService {

    /**
     * Creates gift certificate.
     *
     * @param requestGiftCertificateDto {@code RequestGiftCertificateDto} which represents {@code GiftCertificate}
     *                                  for creation.
     * @return {@code ResponseGiftCertificateDto} which represents created {@code GiftCertificate}.
     * @throws ResourceAlreadyExistsException if certificate with the same name is already exists.
     */
    ResponseGiftCertificateDto createGiftCertificate(RequestGiftCertificateDto requestGiftCertificateDto);

    /**
     * Finds gift certificate by id.
     *
     * @param id {@code GiftCertificate}'s id.
     * @return {@code ResponseGiftCertificateDto} which represents found {@code GiftCertificate}.
     * @throws ResourceNotFoundException if gift certificate with that id doesn't exist.
     */
    ResponseGiftCertificateDto findGiftCertificateById(Integer id);

    /**
     * Deletes gift certificate by id.
     *
     * @param id {@code GiftCertificate}'s id.
     * @return {@code true} if certificate was successfully deleted.
     * @throws ResourceNotFoundException if gift certificate with that id doesn't exist.
     */
    boolean deleteGiftCertificate(Integer id);

    /**
     * Updates gift certificate.
     *
     * @param requestGiftCertificateDto {@code RequestGiftCertificateDto} wich represents {@code GiftCertificate}
     *                                  with updated values.
     * @param id                        {@code GiftCertificate}'s id to update.
     * @return {@code ResponseGiftCertificateDto} which represents updated certificate.
     * @throws ResourceAlreadyExistsException if gift certificate with such name is already exist.
     */
    ResponseGiftCertificateDto updateGiftCertificate(RequestGiftCertificateDto requestGiftCertificateDto, Integer id);


    /**
     * Finds all gift certificates corresponding to given page, number of records per page and sorting/searching parameters.
     *
     * @param giftCertificateCriteria {@code GiftCertificateCriteria} which contains values by which to sort and search.
     * @param pagination              {@code Pagination} which contains page number and records per page amount.
     * @return found certificates.
     */
    List<ResponseGiftCertificateDto> findAllGiftCertificates(GiftCertificateCriteria giftCertificateCriteria, Pagination pagination);

    /**
     * Finds quantity of all certificates.
     *
     * @return {@code Long} quantity.
     */
    Integer findQuantity(GiftCertificateCriteria giftCertificateCriteria);
}
