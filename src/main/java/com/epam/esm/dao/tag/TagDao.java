package com.epam.esm.dao.tag;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.entity.Tag;

import java.util.Optional;

/**
 * {@code Tag} dao interface
 */
public interface TagDao extends CommonDao<Tag, Integer> {

    /**
     * Deletes tag belonged to gift certificate in datasource.
     *
     * @param id {@code Tag}'s id.
     * @return {@code true} if {@code Tag} is deleted from gift certificate.
     */
    boolean deleteGiftCertificateTag(Integer id);

    /**
     * Finds {@code Tag} by its name in datasource.
     *
     * @param tagName {@code Tag}'s name.
     * @return {@code Optional<Tag>}.
     */
    Optional<Tag> findTagByName(String tagName);

    /**
     * Finds the most widely used tag of a user with the highest cost of all purchases.
     *
     * @return {@code Tag}.
     */
    Tag findTheMostPopularTagInUserWithTheHighestCostOfPurchases();

    /**
     * Finds amount of all {@code Tag} stored in datasource
     *
     * @return {@code Long} amount.
     */
    Long findQuantity();
}
