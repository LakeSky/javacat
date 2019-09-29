package com.kzh.sys.service.common;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.dao.ItemDao;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.enums.UpState;
import com.kzh.sys.model.Item;
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
public class ItemService extends BaseService<Item> {
    private static final Logger logger = Logger.getLogger(ItemService.class);

    @Resource
    private ItemDao itemDao;
    @Resource
    private UserService userService;
    @Resource
    private UserDao userDao;

    @Override
    public GenericRepository getDao() {
        return itemDao;
    }

    public Page<Item> page(Item entity, WorldPage worldPage) throws Exception {
        List<SimpleExpression> expressions = entity.initExpressions();
        expressions.add(Restrictions.eq("delFlag", false, true));
        Page<Item> page = itemDao.findByPage(expressions, worldPage.getPageRequest());
        return page;
    }

    public Result saveItem(Item entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Item tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = itemDao.save(entity);
        } else {
            //编辑
            Item item = itemDao.getOne(entity.getId());
            item.initModifyFields(entity);
            tDb = itemDao.save(item);
        }
        return new Result(true, "成功", tDb);
    }

    public void del(String[] ids) {
        if (ArrayUtils.isNotEmpty(ids)) {
            List<SimpleExpression> expressions = new ArrayList<>();
            expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
            List<Item> os = itemDao.findAll(expressions);
            if (CollectionUtil.isNotEmpty(os)) {
                itemDao.delete(os);
            }
        }
    }

    public void delSoft(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Item> os = itemDao.findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Item o : os) {
                o.setDelFlag(true);
            }
            itemDao.save(os);
        }
    }

    public Item get(String id) {
        Item o = itemDao.findOne(id);
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

    public List<Item> getCanSelect(String username) {
        User user = userService.getByUsername(username);
        List<SimpleExpression> expressions = dataExpressions(user);
        expressions.add(Restrictions.eq("upState", UpState.ON, true));
        return itemDao.findAll(expressions);
    }
}
