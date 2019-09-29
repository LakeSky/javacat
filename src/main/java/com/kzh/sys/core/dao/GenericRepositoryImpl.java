package com.kzh.sys.core.dao;

import com.kzh.sys.core.exception.WorldDaoException;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@NoRepositoryBean //必须的
public class GenericRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericRepository<T, ID> {
    private static final Logger logger = Logger.getLogger(GenericRepository.class);
    private final EntityManager entityManager;
    private final Class<T> entityClass;
    private final String entityName;
    private final JpaEntityInformation entityInformation;

    /**
     * 构造函数
     *
     * @param entityInformation
     * @param entityManager
     */
    public GenericRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityClass = entityInformation.getJavaType();
        this.entityName = entityInformation.getEntityName();
        this.entityInformation = entityInformation;
    }

    /**
     * 构造函数
     *
     * @param domainClass
     * @param entityManager
     */
    public GenericRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        this(JpaEntityInformationSupport.getMetadata(domainClass, entityManager), entityManager);
    }

    @Override
    public T findOne(ID id) throws WorldDaoException {
        try {
            return super.findOne(id);
        } catch (Exception e) {
            throw new WorldDaoException("findOne is error", e);
        }
    }

    @Override
    public Object getOne(Class clazz, Object id) {
        Assert.notNull(id, "The given id must not be null!");
        return entityManager.getReference(clazz, id);
    }

    @Override
    public Object findOne(Class clazz, Object id) {
        return entityManager.find(clazz, id);
    }

    @Override
    public void delete(T entity) {
        try {
            super.delete(entity);
        } catch (Exception e) {
            throw new WorldDaoException("delete   system error!", e);
        }
    }

    @Override
    public void deleteObject(Object entity) {
        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        } catch (Exception e) {
            throw new WorldDaoException("delete   system error!", e);
        }
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        try {
            super.delete(entities);
        } catch (Exception e) {
            throw new WorldDaoException("delete   entities error!", e);
        }

    }

    @Override
    public <S extends T> S save(S entity) throws WorldDaoException {
        try {
            return super.save(entity);
        } catch (Exception e) {
            throw new WorldDaoException("save   system error!", e);
        }
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) throws WorldDaoException {
        try {
            return super.save(entities);
        } catch (Exception e) {
            throw new WorldDaoException("save  entities error!", e);
        }
    }


    @Override
    public List<T> findAll(List<SimpleExpression> expressions) throws WorldDaoException {
        return findAll(expressions, null);
    }

    @Override
    public int getCount(List<SimpleExpression> expressions) throws WorldDaoException {
        try {
            String whereHql = SqlBuildUtil.buildWhereQuery(expressions);
            String hql = "select count(*) from " + entityName + " o where 1=1";
            Query query = createQuery(hql + whereHql, null);

            Object o = query.getSingleResult();
            int count = 0;
            try {
                count = Integer.parseInt(o.toString());
            } catch (Exception e) {
                logger.error("getCount parseInt error!", e);
            }
            return count;
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public Query createSQLQuery(String sql) {
        try {
            return this.entityManager.createNativeQuery(sql);
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public Query createHQLQuery(String hql) {
        try {
            return this.entityManager.createQuery(hql);
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public void deleteBySql(String sql) {
        try {
            Query query = this.entityManager.createNativeQuery(sql);
            query.executeUpdate();
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public void deleteByHql(String hql) {
        try {
            Query query = this.entityManager.createQuery(hql);
            query.executeUpdate();
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public List<T> findByHql(String hql, Map<String, Object> queryParams) {
        try {
            Query query = createQuery(hql, null);
            List<T> list = (List<T>) query.getResultList();
            if (queryParams != null && queryParams.size() > 0)
                setQueryParams(query, queryParams);
            return list;
        } catch (Exception e) {
            throw new WorldDaoException("findByHql is error", e);
        }
    }

    @Override
    public List<T> findBySql(String sql, Map<String, Object> queryParams) {
        try {
            logger.info(sql);
            Query query = entityManager.createNativeQuery(sql);
            if (queryParams != null && queryParams.size() > 0)
                setQueryParams(query, queryParams);
            List<T> list = (List<T>) query.getResultList();
            return list;
        } catch (Exception e) {
            throw new WorldDaoException("findByHql is error", e);
        }
    }

    @Override
    public Object saveObject(Object obj) {
        if (entityInformation.isNew(obj)) {
            entityManager.persist(obj);
            return obj;
        } else {
            return entityManager.merge(obj);
        }
    }

    @Override
    public List<T> findByField(String field, Object value) throws WorldDaoException {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.eq(field, value, true));
        return findAll(expressions);
    }

    @Override
    public List<T> findAll(List<SimpleExpression> expressions, Sort sort) throws WorldDaoException {
        try {

            String whereHql = SqlBuildUtil.buildWhereQuery(expressions);
            String orderHql = buildSortQuery(sort);

            String hql = "from " + entityName + " o where 1=1";
            Query query = createQuery(hql + whereHql + orderHql, null);

            List<T> list = (List<T>) query.getResultList();
            return list;
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public List<T> findByHql(String hql) throws WorldDaoException {
        try {
            Query query = createQuery(hql, null);
            List<T> list = (List<T>) query.getResultList();
            return list;
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public List<T> findByHql(String hql, Integer limit) throws WorldDaoException {
        try {
            Query query = createQuery(hql, null);
            query.setMaxResults(limit);
            List<T> list = (List<T>) query.getResultList();
            return list;
        } catch (Exception e) {
            throw new WorldDaoException("findAll is error", e);
        }
    }

    @Override
    public List<T> findAllWithHql(String hql, Map<String, Object> queryParams) throws WorldDaoException {
        try {
            Query query = createQuery(hql, queryParams);
            List<T> list = (List<T>) query.getResultList();
            return list;
        } catch (Exception e) {
            throw new WorldDaoException("findAllWithHql is error", e);
        }
    }

    @Override
    public Page<T> findByPage(List<SimpleExpression> expressions, Pageable pageable) throws WorldDaoException {

        return findByPage(expressions, buildSortQuery(pageable.getSort()), pageable);

    }

    @Override
    public Page<T> findByPage(List<SimpleExpression> expressions, String orderBy, Pageable pageable) throws WorldDaoException {

        try {
            String whereHql = SqlBuildUtil.buildWhereQuery(expressions);
            String orderHql = "";
            if (orderBy.toLowerCase().contains("order")) {
                orderHql = orderBy;
            } else {
                orderHql = " order by " + orderBy;
            }

            String hql = "SELECT count(*) FROM " + entityName + " o WHERE 1=1 ";
            Query query = createQuery(hql + whereHql, null);
            long total = ((Long) query.getSingleResult()).longValue();


            hql = "SELECT o FROM " + entityName + " o WHERE 1=1 ";
            query = createQuery(hql + whereHql + orderHql, null);
            query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

            return new PageImpl<T>(query.getResultList(), pageable, total);
        } catch (Exception e) {
            throw new WorldDaoException("findByPage is error", e);
        }
    }

    @Override
    public Page<T> findByPage(String selHql, String whereHql, String orderBy, Pageable pageable) throws WorldDaoException {

        try {
            String orderHql = " " + orderBy;

            String countHql = "SELECT count(*) FROM " + entityName + " o WHERE 1=1 ";
            Query query = createQuery(countHql + whereHql, null);
            long total = ((Long) query.getSingleResult()).longValue();


            query = createQuery(selHql + whereHql + orderHql, null);
            query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

            return new PageImpl<T>(query.getResultList(), pageable, total);
        } catch (Exception e) {
            throw new WorldDaoException("findByPage is error", e);
        }
    }

    public Page<T> findByPage(List<SimpleExpression> expressions, String joinTable, String joinSql, String orderBy, Pageable pageable) throws WorldDaoException {
        try {
            if (joinTable == null) {
                joinTable = "";
            }
            if (joinSql == null) {
                joinSql = "";
            }
            String whereHql = SqlBuildUtil.buildWhereQuery(expressions);
            String orderHql = orderBy;

            String hql = "SELECT count(*) FROM " + entityName + " o " + joinTable + " WHERE 1=1 ";
            Query query = createQuery(hql + joinSql + whereHql, null);
            long total = ((Long) query.getSingleResult()).longValue();


            hql = "SELECT o FROM " + entityName + " o " + joinTable + " WHERE 1=1 ";
            query = createQuery(hql + joinSql + whereHql + orderHql, null);
            query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

            return new PageImpl<T>(query.getResultList(), pageable, total);
        } catch (Exception e) {
            throw new WorldDaoException("findByPage is error", e);
        }
    }

    private Query createQuery(String hql, Map<String, Object> queryParams) {
        logger.info("hql:[" + hql + "]");
        Query query = entityManager.createQuery(hql);
        if (queryParams != null && queryParams.size() > 0)
            setQueryParams(query, queryParams);
        return query;
    }

    @Override
    public Page<T> findByPageWithWhereHql(String whereHql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException {

        try {
            String hql = "SELECT count(*) FROM " + entityName + " o WHERE 1=1 ";
            if (whereHql == null) {
                whereHql = "";
            }
            Query query = entityManager.createQuery(hql + whereHql);
            setQueryParams(query, queryParams);
            long total = ((Long) query.getSingleResult()).longValue();


            hql = "SELECT o FROM " + entityName + " o WHERE 1=1 ";
            query = entityManager.createQuery(hql + whereHql);
            setQueryParams(query, queryParams);
            query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

            return new PageImpl<T>(query.getResultList(), pageable, total);
        } catch (Exception e) {
            throw new WorldDaoException("findByPageWithWhereHql is error", e);
        }
    }


    @Override
    public Page<T> findByPageWithHql(String hql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException {

        try {
            Assert.hasText(hql);
            int fromIdx = hql.toLowerCase().indexOf(" from ");
            Assert.isTrue(fromIdx != -1, "queryString [" + hql + "] must has a keyword 'from'.");
            String countHql = hql.substring(fromIdx);
            Query query = entityManager.createQuery("SELECT count(*)   " + countHql);
            setQueryParams(query, queryParams);
            long total = ((Long) query.getSingleResult()).longValue();
            String orderHql = buildSortQuery(pageable.getSort());
            query = entityManager.createQuery(hql + orderHql);
            setQueryParams(query, queryParams);
            query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

            return new PageImpl<T>(query.getResultList(), pageable, total);
        } catch (Exception e) {
            throw new WorldDaoException("findByPageWithHql is error", e);
        }
    }

    @Override
    public Page findByPageWithHql(String hql, Pageable pageable) throws WorldDaoException {
        try {
            Assert.hasText(hql);
            int fromIdx = hql.toLowerCase().indexOf("from ");
            Assert.isTrue(fromIdx != -1, "queryString [" + hql + "] must has a keyword 'from'.");
            String countHql = hql.substring(fromIdx);
            logger.info("=====findByPageWithHql=====hql:[" + "SELECT count(*)   " + countHql + "]");
            Query query = entityManager.createQuery("SELECT count(*)   " + countHql);
            long total = ((Long) query.getSingleResult()).longValue();
            String orderHql = buildSortQuery(pageable.getSort());
            query = entityManager.createQuery(hql + orderHql);
            query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

            return new PageImpl(query.getResultList(), pageable, total);
        } catch (Exception e) {
            throw new WorldDaoException("findByPageWithHql is error", e);
        }
    }

    @Override
    public Page<T> findByPageWithSql(String nativeSql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException {

        try {
            Query query = entityManager.createNativeQuery("SELECT count(*) FROM (" + nativeSql + ")");
            setQueryParams(query, queryParams);
            long total = ((Long) query.getSingleResult()).longValue();

            query = entityManager.createNativeQuery(nativeSql);
            setQueryParams(query, queryParams);
            query.setFirstResult(pageable.getOffset()).setMaxResults(pageable.getPageSize());

            return new PageImpl<T>(query.getResultList(), pageable, total);
        } catch (Exception e) {
            throw new WorldDaoException("findByPageWithSql is error", e);
        }
    }

    private void setQueryParams(Query query, Map<String, Object> queryParams) {
        if (queryParams != null && queryParams.size() > 0) {
            for (String key : queryParams.keySet()) {
                query.setParameter(key, queryParams.get(key));
            }
        }
    }

    private String buildSortQuery(Sort sort) {
        StringBuffer orderBy = new StringBuffer();
        if (sort != null) {
            Iterator<Sort.Order> iterator = sort.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                if (index == 0) {
                    orderBy.append(" ORDER BY ");
                }
                index++;
                Sort.Order order = iterator.next();
                orderBy.append(order.getProperty()).append(" ").append(order.getDirection().name()).append(",");
            }
            orderBy.deleteCharAt(orderBy.length() - 1);
        }

        return orderBy.toString();
    }

    @Override
    public List<T> findAllWithFullSql(Class<T> entityClass, String nativeSql) throws WorldDaoException {
        try {
            logger.info("====findAllWithFullSql======sql:[" + nativeSql + "]");
            //创建原生SQL查询QUERY实例,指定了返回的实体类型
            Query query = entityManager.createNativeQuery(nativeSql, entityClass);
            //执行查询，返回的是实体列表,
            List<T> EntityList = query.getResultList();
            return EntityList;
        } catch (Exception e) {
            throw new WorldDaoException("findAllWithFullSql is error", e);
        }
    }

    public String getUniqueResultBySql(String nativeSql, Map<String, Object> queryParams) throws WorldDaoException {
        try {
            logger.info("====getUniqueResultBySql======sql:[" + nativeSql + "]");
            Query query = entityManager.createNativeQuery(nativeSql);
            for (String key : queryParams.keySet()) {
                query.setParameter(key, queryParams.get(key));
            }
            //执行查询，返回的是实体列表,
            String result = (String) query.getSingleResult();
            return result;
        } catch (Exception e) {
            throw new WorldDaoException("getUniqueResultBySql is error", e);
        }
    }

    @Override
    public List<T> findAllWithFullHql(String hql, Pageable pageable) throws WorldDaoException {
        try {
            if (hql != null && hql.length() > 0) {
                logger.info("====findAllWithFullHql======hql:[" + hql + "]");
                Query query = this.entityManager.createQuery(hql);
                if (pageable != null) {
                    query.setFirstResult(pageable.getOffset());
                    query.setMaxResults(pageable.getPageSize());
                }
                return query.getResultList();
            } else {
                throw new WorldDaoException("findAllWithFullHql is error,hql is null!");
            }
        } catch (Exception e) {
            throw new WorldDaoException("findAllWithFullHql is error", e);
        }
    }

    @Override
    public List<Object[]> findFieldsWithHql(String hql, Map<String, Object> queryParams, Pageable pageable) throws WorldDaoException {
        try {
            if (hql != null && hql.length() > 0) {
                logger.info("====findAllWithFullHql======hql:[" + hql + "]");
                Query query = this.entityManager.createQuery(hql);
                setQueryParams(query, queryParams);
                if (pageable != null) {
                    query.setFirstResult(pageable.getOffset());
                    query.setMaxResults(pageable.getPageSize());
                }
                return query.getResultList();
            } else {
                throw new WorldDaoException("findAllWithFullHql is error,hql is null!");
            }
        } catch (Exception e) {
            throw new WorldDaoException("findAllWithFullHql is error", e);
        }
    }

    @Override
    public int getCountWithFullHql(String hql) throws WorldDaoException {
        try {
            if (hql != null && hql.length() > 0) {
                logger.info("=====getCountWithFullHql=====hql:[" + hql + "]");
                List<BigInteger> o = this.entityManager.createQuery(hql).getResultList();
                return o.get(0).intValue();
            } else {
                throw new WorldDaoException("getCountWithFullHql is error,hql is null!");
            }
        } catch (Exception e) {
            throw new WorldDaoException("getCountWithFullHql is error", e);
        }
    }

    @Override
    public List<T> findAllWithFullSql(String nativeSql, Pageable pageable) throws WorldDaoException {
        try {
            if (nativeSql != null && nativeSql.length() > 0) {
                logger.info("====findAllWithFullSql======sql:[" + nativeSql + "]");
                Query query = this.entityManager.createNativeQuery(nativeSql);
                if (pageable != null) {
                    query.setFirstResult(pageable.getOffset());
                    query.setMaxResults(pageable.getPageSize());
                }
                return query.getResultList();
            } else {
                throw new WorldDaoException("findAllWithFullSql is error,nativeSql is null!");
            }
        } catch (Exception e) {
            throw new WorldDaoException("findAllWithFullSql is error", e);
        }
    }

    @Override
    public List<Object[]> findFieldsWithFullSql(String nativeSql, Pageable pageable) throws WorldDaoException {
        try {
            if (nativeSql != null && nativeSql.length() > 0) {
                logger.info("====findAllWithFullSql======sql:[" + nativeSql + "]");
                Query query = this.entityManager.createNativeQuery(nativeSql);
                if (pageable != null) {
                    query.setFirstResult(pageable.getOffset());
                    query.setMaxResults(pageable.getPageSize());
                }
                return query.getResultList();
            } else {
                throw new WorldDaoException("findAllWithFullSql is error,nativeSql is null!");
            }
        } catch (Exception e) {
            throw new WorldDaoException("findAllWithFullSql is error", e);
        }
    }

    @Override
    public int getCountWithFullSql(String nativeSql) throws WorldDaoException {
        try {
            if (nativeSql != null && nativeSql.length() > 0) {
                logger.info("====getCountWithFullSql======sql:[" + nativeSql + "]");
                List<BigInteger> o = this.entityManager.createNativeQuery(nativeSql).getResultList();
                return o.get(0).intValue();
            } else {
                throw new WorldDaoException("getCountWithFullSql is error,nativeSql is null!");
            }
        } catch (Exception e) {
            throw new WorldDaoException("getCountWithFullSql is error", e);
        }
    }

    @Override
    public int updateById(Class clazz, String value, String id) {
        return 0;
    }

    @Override
    public void detach(Object o) {
        entityManager.detach(o);
    }
}
