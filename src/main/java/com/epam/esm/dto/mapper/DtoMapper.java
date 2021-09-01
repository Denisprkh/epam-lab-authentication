
package com.epam.esm.dto.mapper;

public interface DtoMapper<T, K> {

    T toDto(K k);

    K toModel(T t);

}