package com.kzh.sys.model;

import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.service.generate.auto.QField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//角色与资源关系表
@QClass(name = "角色与资源关系表")
@Entity
@Table(name = "sys_role_resource")
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleResource extends BaseEntity {
    @QField(name = "资源url")
    @Column(name = "url")
    private String url;

    @QField(name = "角色Id")
    @Column(name = "role_id")
    private String roleId;
}
