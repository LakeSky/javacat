package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.RoleResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleResourceDao extends GenericRepository<RoleResource, String> {
    List<RoleResource> findByRoleId(String roleId);

    List<RoleResource> findByUrl(String url);
}
