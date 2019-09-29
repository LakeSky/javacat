package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Api;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiDao extends GenericRepository<Api, String> {
    Api findByUrl(String url);
}
