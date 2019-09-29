package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Log;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDao extends GenericRepository<Log, String> {

}
