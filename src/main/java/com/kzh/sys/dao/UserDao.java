package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserDao extends GenericRepository<User, String> {
    User findByUsername(String username);

    User findByPhone(String phone);

    User findByOpenid(String openid);

    List<User> findByIdIn(Collection<String> ids);
}
