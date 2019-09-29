package com.kzh.busi.service;

import com.kzh.busi.dao.EmployeeDao;
import com.kzh.busi.model.Employee;
import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.enums.UpState;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.pojo.WorldPage;
import com.kzh.sys.service.sys.BaseService;
import com.kzh.sys.service.sys.UserService;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.SysUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeService extends BaseService<Employee> {
    private static final Logger logger = Logger.getLogger(EmployeeService.class);

    @Resource
    private EmployeeDao employeeDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;

    @Override
    public GenericRepository getDao() {
        return employeeDao;
    }

    public Page<Employee> page(Employee entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Employee> page = employeeDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public Result saveEmployee(Employee entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Employee tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = employeeDao.save(entity);
        } else {
            //编辑
            Employee employee = employeeDao.getOne(entity.getId());
            employee.initModifyFields(entity);
            tDb = employeeDao.save(employee);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Employee> os = employeeDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                employeeDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Employee> os = employeeDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Employee o : os) {
                o.setDelFlag(true);
            }
            employeeDao.save(os);
        }
    }

    public Employee get(String id) {
        Employee o = employeeDao.findOne(id);
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

    public List<Employee> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return employeeDao.findAll(expressions);
    }
}
