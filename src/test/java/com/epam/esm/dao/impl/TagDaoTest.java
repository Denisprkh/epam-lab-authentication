package com.epam.esm.dao.impl;

import com.epam.esm.dao.tag.TagDao;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest()
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Test
    void findByIdTest() {
        String expectedTagName = "sport";

        Tag resultTag = tagDao.findById(1);
        String resultTagName = resultTag.getName();

        assertEquals(expectedTagName, resultTagName);
    }

    @Test
    void findByIdShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> tagDao.findById(5));
    }

    @Test
    @Transactional
    void createTest() {
        Tag tagForCreation = new Tag("Name");
        String expectedTagName = "Name";

        Tag resultTag = tagDao.create(tagForCreation);

        assertEquals(expectedTagName, resultTag.getName());
    }

    @Test
    void findAllTest() {
        int expectedListSize = 3;

        List<Tag> allTags = tagDao.findAll(0, 10);

        assertEquals(expectedListSize, allTags.size());
    }

    @Test
    @Transactional
    void deleteTagTest() {
        boolean isDeleted = tagDao.delete(1);

        assertTrue(isDeleted);
    }

    @Test
    void deleteTagTestShouldThrowException() {
        assertThrows(ResourceNotFoundException.class, () -> tagDao.delete(5));
    }

    @Test
    void findQuantityTest() {
        long expectedSize = 3;

        long resultSize = tagDao.findQuantity();

        assertEquals(expectedSize, resultSize);
    }

    @Test
    @Transactional
    void deleteGiftCertificateTagTestShouldReturnTrue() {
        boolean isDeleted = tagDao.deleteGiftCertificateTag(1);

        assertTrue(isDeleted);
    }

    @Test
    @Transactional
    void deleteGiftCertificateTagTestShouldReturnFalse() {
        boolean isDeleted = tagDao.deleteGiftCertificateTag(5);

        assertFalse(isDeleted);
    }

    @Test
    void findTagByName() {
        Tag expectedTag = new Tag();
        expectedTag.setName("sport");
        expectedTag.setId(1);

        Tag resultTag = tagDao.findTagByName("sport").get();

        assertEquals(expectedTag, resultTag);
    }

}
