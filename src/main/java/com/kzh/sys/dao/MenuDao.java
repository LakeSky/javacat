package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Menu;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("resourceDao")
public interface MenuDao extends GenericRepository<Menu, String> {
    List<Menu> findByParentId(String parentId);
}