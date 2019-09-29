package com.kzh.sys.core.dao;

import com.kzh.sys.core.exception.WorldDaoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    List<T> findByField(String field, Object value) throws WorldDaoException;

    List<T> findAll(List<SimpleExpression> expressions, Sort sort) throws WorldDaoException;

    List<T> findAll(List<SimpleExpression> expressions) throws WorldDaoException;

    Object getOne(Class clazz, Object id);

    Object findOne(Class clazz, Object id);

    Page<T> findByPage(List<SimpleExpression> expressions, Pageable pageable) throws WorldDaoException;

    Page<T> findByPageWithSql(String sql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException;

    Page<T> findByPageWithWhereHql(String whereHql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException;

    Page<T> findByPage(List<SimpleExpression> expressions, String orderBy, Pageable pageable) throws WorldDaoException;

    public Page<T> findByPage(String selHql, String whereHql, String orderBy, Pageable pageable) throws WorldDaoException;

    Page<T> findByPage(List<SimpleExpression> expressions, String joinTable, String joinSql, String orderBy, Pageable pageable) throws WorldDaoException;

    List<T> findAllWithFullSql(Class<T> entityClass, String sql) throws WorldDaoException;

    String getUniqueResultBySql(String sql, Map<String, Object> queryParams) throws WorldDaoException;

    Page<T> findByPageWithHql(String hql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException;

    Page findByPageWithHql(String hql, Pageable pageable) throws WorldDaoException;

    List<T> findAllWithHql(String hql, Map<String, Object> queryParams) throws WorldDaoException;

    int getCountWithFullSql(String nativeSql) throws WorldDaoException;

    List<T> findAllWithFullSql(String nativeSql, Pageable pageable) throws WorldDaoException;

    List<Object[]> findFieldsWithFullSql(String nativeSql, Pageable pageable) throws WorldDaoException;

    int getCountWithFullHql(String hql) throws WorldDaoException;

    List<T> findAllWithFullHql(String hql, Pageable pageable) throws WorldDaoException;

    List<Object[]> findFieldsWithHql(String hql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException;

    int getCount(List<SimpleExpression> expressions) throws WorldDaoException;

    Query createSQLQuery(String sql);

    Query createHQLQuery(String hql);

    void deleteBySql(String sql);

    void deleteByHql(String hql);

    void deleteObject(Object entity);

    List<T> findByHql(String hql, Map<String, Object> queryParams);

    List<T> findBySql(String sql, Map<String, Object> queryParams);

    List<T> findByHql(String hql) throws WorldDaoException;

    List<T> findByHql(String hql, Integer limit) throws WorldDaoException;

    Object saveObject(Object obj);

    int updateById(Class clazz, String value, String id);

    void detach(Object o);
}