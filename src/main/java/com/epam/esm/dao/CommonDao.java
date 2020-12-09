package com.epam.esm.dao;

import com.epam.esm.exception.ResourceNotFoundException;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * Common dao interface.
 *
 * @param <T> Some entity to work with.
 * @param <K> Entities id.
 */
public interface CommonDao<T, K> {

    /**
     * Creates object in datasource.
     *
     * @param t object to create.
     * @return created object.
     */
    T create(T t);

    /**
     * Returns resource found by its id.
     *
     * @param id resources id.
     * @return found resource.
     * @throws ResourceNotFoundException if resource is not found.
     */
    T findById(K id);

    /**
     * Deletes resource in datasource by its id.
     *
     * @param id Resources id.
     * @return {@code true} if resource is deleted.
     * @throws ResourceNotFoundException if resource is not exist.
     */
    boolean delete(K id);

    /**
     * Updates object in datasource.
     *
     * @param t object with updated values.
     * @return updated object.
     */
    T update(T t);

    /**
     * Returns resources matching parameters.
     *
     * @param startOfRecords       start position of matching objects in datasource.
     * @param recordsPerPageAmount amount of matching objects.
     * @return {@code List<T>} of objects.
     */
    List<T> findAll(int startOfRecords, int recordsPerPageAmount);

}
