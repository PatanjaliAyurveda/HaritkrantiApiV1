package com.bharuwa.haritkranti.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * @author anuragdhunna
 *
 * Generic Interface for Mongo Template Methods
 */
public interface GenericMongoTemplate {

    <T> T findById(String id, Class<T> clazz);

    <T> List<T> findAll(Class<T> clazz);

    <T> T findByKey(String key, String value, Class<T> clazz);

    <T> Page<T> paginationWithQuery(int page, int size, Class<T> clazz);

    <T> Page<T> paginationWithQuery(int page, int size, Query query, Class<T> clazz);

    <T> List<T> findAllById(String id, Class<T> clazz);
}
