package co.zonetechpark.booktest.booktest.service.impl;

import co.zonetechpark.booktest.booktest.service.JpaService;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Service
@Slf4j
public class JpaServiceImpl implements JpaService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public <E> long count(Class<E> clazz) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<E> root = criteriaQuery.from(clazz);
        criteriaQuery.select(criteriaBuilder.count(root));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Transactional(readOnly = true)
    @Override
    public <E> Optional<E> findById(Class<E> clazz, Object id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public <E> E persist(E e) {
        entityManager.persist(e);
        return e;
    }

    @Override
    public <E> E merge(E e) {
        return entityManager.merge(e);
    }

    @Override
    public void remove(Object object) {
        entityManager.remove(object);
    }

    @Override
    public <E> JPAQuery<E> startJPAQeuryFrom(EntityPath<E> entityPath) {
        return new JPAQuery<E>(entityManager).from(entityPath);
    }

    @Override
    public <E> QueryResults<E> fetchResults(JPAQuery<E> jpaQuery) {
        return jpaQuery.fetchResults();
    }
}
