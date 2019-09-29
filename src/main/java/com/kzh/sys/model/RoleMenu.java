package com.kzh.sys.model;

import com.kzh.sys.service.generate.auto.QClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色菜单关系
 */
@QClass(name = "角色菜单关系")
@Entity
@Table(name = "sys_role_menu")
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleMenu extends BaseEntity {
    @Column(name = "menu_id")
    private String menuId;
    @Column(name = "role_id")
    private String roleId;
}
