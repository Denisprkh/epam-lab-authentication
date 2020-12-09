package com.epam.esm.dao.impl;

import com.epam.esm.dao.giftcertificate.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.GiftCertificateCriteria;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GiftCertificateDaoTest {

    @Autowired
    private GiftCertificateDao giftCertificateDao;

    @Test
    void findByIdTest() {
        String expectedName = "Gym";

        GiftCertificate resultGiftCertificate = giftCertificateDao.findById(1);
        String resultGiftCertificateName = resultGiftCertificate.getName();

        assertEquals(expectedName, resultGiftCertificateName);
    }

    @Test
    void findByIdTestShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateDao.findById(5));
    }

    @Test
    @Transactional
    void createTest() {
        GiftCertificate giftCertificateForCreation = new GiftCertificate();
        giftCertificateForCreation.setName("Name");
        giftCertificateForCreation.setDuration(Duration.ofDays(5));
        giftCertificateForCreation.setPrice(new BigDecimal("10.5"));
        giftCertificateForCreation.setCreateDate(stringToLocalDateTime("2020-11-22 18:17:54"));
        giftCertificateForCreation.setLastUpdateDate(stringToLocalDateTime("2020-11-22 18:17:54"));
        giftCertificateForCreation.setDescription("desc");
        int expectedId = 4;

        int resultId = giftCertificateDao.create(giftCertificateForCreation).getId();

        assertEquals(expectedId, resultId);

    }

    @Test
    @Transactional
    void updateTest() {
        GiftCertificate giftCertificateWithUpdatedValues = new GiftCertificate();
        giftCertificateWithUpdatedValues.setId(1);
        giftCertificateWithUpdatedValues.setName("newName");
        giftCertificateWithUpdatedValues.setDuration(Duration.ofDays(5));
        giftCertificateWithUpdatedValues.setPrice(new BigDecimal("10.5"));
        giftCertificateWithUpdatedValues.setCreateDate(stringToLocalDateTime("2020-11-04 22:18:24"));
        giftCertificateWithUpdatedValues.setLastUpdateDate(stringToLocalDateTime("2020-11-04 22:18:24"));
        giftCertificateWithUpdatedValues.setDescription("Simple description");
        String expectedUpdatedName = "newName";

        String resultName = giftCertificateDao.update(giftCertificateWithUpdatedValues).getName();

        assertEquals(expectedUpdatedName, resultName);

    }

    @Test
    void findQuantityTest() {
        int expectedQuantity = 3;

        int resultQuantity = giftCertificateDao.findQuantity(new GiftCertificateCriteria());

        assertEquals(expectedQuantity, resultQuantity);
    }

    @Test
    @Transactional
    void deleteTestShouldReturnTrue() {
        boolean isDeleted = giftCertificateDao.delete(1);

        assertTrue(isDeleted);
    }

    @Test
    @Transactional
    void deleteTestShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> giftCertificateDao.delete(5));
    }

    @Test
    void findGiftCertificateByNameTest() {
        int expectedGiftCertificateId = 2;

        GiftCertificate resultGiftCertificate = giftCertificateDao.findGiftCertificateByName("Provenance").get();
        int resultCertificateId = resultGiftCertificate.getId();

        assertEquals(expectedGiftCertificateId, resultCertificateId);

    }

    private LocalDateTime stringToLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }
}
