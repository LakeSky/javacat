package com.kzh.sys.service.sys;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.model.BaseEntity;
import com.kzh.sys.util.CollectionUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public abstract class BaseService<T extends BaseEntity> {
    private static final Logger logger = Logger.getLogger(BaseService.class);

    public abstract GenericRepository getDao();

    public List<T> getAll() {
        List<T> ts = getDao().findAll();
        return ts;
    }

    public T save(T t) {
        return (T) getDao().save(t);
    }

    public T get(String id) {
        return (T) (getDao().findOne(id));
    }

    public void del(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List os = getDao().findAll(expressions);
        getDao().delete(os);
    }

    public List<T> findByField(String field, Object value) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.eq(field, value, true));
        return getDao().findAll(expressions);
    }
}
