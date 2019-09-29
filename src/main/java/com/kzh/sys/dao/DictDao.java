package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Dict;
import org.springframework.stereotype.Repository;

@Repository
public interface DictDao extends GenericRepository<Dict, String> {

}
