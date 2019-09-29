package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Token;
import org.springframework.stereotype.Repository;

/**
 * Created by gang on 2016/8/2.
 */
@Repository
public interface TokenDao extends GenericRepository<Token, String> {
    Token findByTokenKey(String tokenKey);
}
