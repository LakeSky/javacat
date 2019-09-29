package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Msg;
import org.springframework.stereotype.Repository;

@Repository
public interface MsgDao extends GenericRepository<Msg, String> {
}
