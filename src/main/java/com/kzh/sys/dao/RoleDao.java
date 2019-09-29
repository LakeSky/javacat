package com.kzh.sys.dao;

import com.kzh.sys.core.dao.GenericRepository;
import com.kzh.sys.model.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Qualifier("roleDao")
public interface RoleDao extends GenericRepository<Role, String> {
    List<Role> findByIdIn(String[] ids);

    Role findByRoleKey(String roleKey);

    Role findByRoleName(String roleName);
}
