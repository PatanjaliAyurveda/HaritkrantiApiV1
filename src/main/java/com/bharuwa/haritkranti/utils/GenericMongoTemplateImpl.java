package com.bharuwa.haritkranti.utils;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author anuragdhunna
 */
@Service
public class GenericMongoTemplateImpl implements GenericMongoTemplate {

    private final MongoTemplate mongoTemplate;

    public GenericMongoTemplateImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public <T> T findById(String id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.findOne(query, clazz);
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        return mongoTemplate.findAll(clazz);
    }

    @Override
    public <T> T findByKey(String key, String value, Class<T> clazz) {
        Query query = new Query(Criteria.where(key).is(value));
        try {
        	mongoTemplate.findOne(query, clazz);
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        return mongoTemplate.findOne(query, clazz);
    }

    @Override
    public <T> List<T> findAllById (String id, Class<T> clazz) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        return mongoTemplate.find(query, clazz);
    }

    @Override
    public <T> Page<T> paginationWithQuery(int page, int size, Class<T> clazz) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(pageable);
        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, clazz),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), clazz));
    }

    @Override
    public <T> Page<T> paginationWithQuery(int page, int size, Query query, Class<T> clazz) {
        Pageable pageable = PageRequest.of(page, size);
        query = query.with(pageable);
        Query finalQuery = Query.of(query).limit(-1).skip(-1);
        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, clazz),
                pageable,
                () -> mongoTemplate.count(finalQuery, clazz));
    }

    //TODO: https://medium.com/swlh/exception-handling-in-java-streams-5947e48f671c
    static <Target, ExObj extends Exception> Consumer<Target> handledConsumer(Consumer<Target> targetConsumer, Class<ExObj> exceptionClazz) {
        return obj -> {
            try {
                targetConsumer.accept(obj);
            } catch (Exception ex) {
                try {
                    ExObj exCast = exceptionClazz.cast(ex);
                    System.err.println(
                            "Exception occured : " + exCast.getMessage());
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }
}
