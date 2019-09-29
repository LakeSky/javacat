package com.kzh.sys.service.sys;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.dao.LogDao;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.enums.SysLogAction;
import com.kzh.sys.enums.UpState;
import com.kzh.sys.model.Log;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
@Transactional
public class LogService extends BaseService<Log> {
    private static final Logger logger = Logger.getLogger(LogService.class);

    @Resource
    private LogDao logDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;

    @Override
    public GenericRepository getDao() {
        return logDao;
    }

    public List<Log> objLog(final String objId) {
        Specification<Log> spec = new Specification<Log>() {
            public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (SysUtil.isNotEmpty(objId)) {
                    Predicate p2 = cb.equal(root.get("objId"), objId);
                    predicates.add(p2);
                }

                query.where(predicates.toArray(new Predicate[predicates.size()]));
                query.orderBy(cb.desc(root.get("createTime")));
                return query.getRestriction();
            }
        };

        List<Log> devices = logDao.findAll(spec);

        return devices;
    }

    public Page<Log> page(Log entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Log> page = logDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public Result saveLog(Log entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Log tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = logDao.save(entity);
        } else {
            //编辑
            Log log = logDao.getOne(entity.getId());
            log.initModifyFields(entity);
            tDb = logDao.save(log);
        }
        return new Result(true, "成功", tDb);
    }

    public Result save(String objId, String category, String action, String content, String detail) {
        Log log = new Log(objId, category, action, content, detail);
        logDao.save(log);
        return new Result(true, "成功");
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Log> os = logDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                logDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Log> os = logDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Log o : os) {
                o.setDelFlag(true);
            }
            logDao.save(os);
        }
    }

    public Log get(String id) {
        Log o = logDao.findOne(id);
        return o;
    }

    public List<SimpleExpression> dataExpressions(User user) {
        List<SimpleExpression> expressions = new ArrayList<>();
        switch (userService.getDataLevel(user)) {
            case ALL:
                break;
            default:
                expressions.add(Restrictions.eq("id", "-1", true));
                break;
        }
        return expressions;
    }

    public List<Log> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return logDao.findAll(expressions);
    }

    public Map<String, Set<String>> categories() {
        Map<String, Set<String>> map = new HashMap<>();
        Set<String> categories = new HashSet<>();
        categories.add("系统");
        Set<String> actions = new HashSet<>();
        for (SysLogAction value : SysLogAction.values()) {
            actions.add(value.getName());
        }
        String hql = "from Log where category!='系统'";
        List<Log> logs = logDao.findByHql(hql);
        if (CollectionUtil.isNotEmpty(logs)) {
            for (Log log : logs) {
                categories.add(log.getCategory());
                actions.add(log.getAction());
            }
        }

        map.put("categories", categories);
        map.put("actions", actions);
        return map;
    }
}
