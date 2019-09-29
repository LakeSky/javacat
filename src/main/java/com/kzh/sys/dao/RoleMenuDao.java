package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.RoleMenu;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


@Repository
public interface RoleMenuDao extends GenericRepository<RoleMenu, String> {
    
    List<RoleMenu> findByRoleIdIn(Collection roleIds);

    List<RoleMenu> findByRoleId(String roleId);
}
