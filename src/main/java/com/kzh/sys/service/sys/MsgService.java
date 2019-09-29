package com.kzh.sys.service.sys;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.core.dao.Restrictions;
import com.kzh.sys.core.dao.SimpleExpression;
import com.kzh.sys.dao.MsgDao;
import com.kzh.sys.dao.UserDao;
import com.kzh.sys.enums.ReadState;
import com.kzh.sys.model.Msg;
import com.kzh.sys.model.User;
import com.kzh.sys.pojo.Result;
import com.kzh.sys.util.CollectionUtil;
import com.kzh.sys.util.DateUtil;
import com.kzh.sys.util.SysUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MsgService extends BaseService<Msg> {
    @Resource
    private MsgDao msgDao;
    @Resource
    private UserDao userDao;

    @Override
    public GenericRepository getDao() {
        return msgDao;
    }

    public Result saveMsg(Msg entity) throws Exception {
        Result result = entity.validateField();
        if (!result.isSuccess()) {
            return result;
        }
        Msg tDb;
        if (SysUtil.isEmpty(entity.getId())) {
            //添加
            tDb = msgDao.save(entity);
        } else {
            //编辑
            Msg Msg = msgDao.getOne(entity.getId());
            Msg.initModifyFields(entity);
            tDb = msgDao.save(Msg);
        }
        return new Result(true, "成功", tDb);
    }

    public void saveMsg(String handler, String objId, String title, String handleAction, String content, String username) {
        if (!handler.equals(username)) {
            Msg msg = new Msg(objId, title, handleAction, content, username);
            msgDao.save(msg);
        }
    }


    public List<Msg> getUnReadMsg(String username, Integer limit) {
        User user = userDao.findByUsername(username);
        Date date = user.getReadMsgTime();
        if (date == null) {
            date = user.getCreateTime();
        }
        String hql = "from Msg t where t.username='" + username + "' and t.createTime > '" + DateUtil.format("yyyy-MM-dd HH:mm:ss", date) + "' order by t.createTime desc";
        List<Msg> msgs = msgDao.findByHql(hql, limit);
        return msgs;
    }

    public long getUnReadMsgCount(String username) {
        User user = userDao.findByUsername(username);
        Date date = user.getReadMsgTime();
        if (date == null) {
            date = user.getCreateTime();
        }
        String hql = "select count(*) from Msg t where t.username='" + username + "' and t.createTime > '" + DateUtil.format("yyyy-MM-dd HH:mm:ss", date) + "' order by t.createTime desc";
        javax.persistence.Query query = msgDao.createHQLQuery(hql);
        return (long) query.getSingleResult();
    }

    public void mark(String[] ids) {
        List<SimpleExpression> expressions = new ArrayList<>();
        expressions.add(Restrictions.in("id", CollectionUtil.arrayToSet(ids), true));
        List<Msg> os = getDao().findAll(expressions);
        if (CollectionUtil.isNotEmpty(os)) {
            for (Msg o : os) {
                o.setReadState(ReadState.YD);
            }
        }

        msgDao.save(os);
    }
}
