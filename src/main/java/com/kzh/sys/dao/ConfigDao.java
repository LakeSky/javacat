package com.kzh.sys.dao;


import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Config;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigDao extends GenericRepository<Config, String> {
    Config findByConfigKey(String configKey);
}
