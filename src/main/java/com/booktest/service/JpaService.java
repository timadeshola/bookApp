package com.booktest.service;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;

import java.util.Optional;

public interface JpaService {

    <E> long count(Class<E> clazz);

    <E>Optional<E> findById(Class<E> clazz, Object id);

    <E> E persist(E e);

    <E> E merge(E e);

    void remove(Object object);

    <E>JPAQuery<E> startJPAQeuryFrom(EntityPath<E> entityPath);

    <E>QueryResults<E> fetchResults(JPAQuery<E> jpaQuery);

}
