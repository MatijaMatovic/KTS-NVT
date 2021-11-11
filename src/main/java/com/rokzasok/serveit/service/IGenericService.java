package com.rokzasok.serveit.service;

import java.util.List;

public interface IGenericService<T> {
    List<T> findAll();

    T findOne(Integer id);

    T save(T entity);

    Boolean deleteOne(Integer id);
}
