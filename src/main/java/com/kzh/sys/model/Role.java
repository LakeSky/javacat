package com.kzh.sys.model;

import com.kzh.busi.enums.DataLevel;
import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldQueryType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 角色
 */
@QClass(name = "角色")
@Entity
@Table(name = "sys_role")
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends BaseEntity {
    @Column(name = "role_key", nullable = false, unique = true)
    @QField(name = "角色代码")
    private String roleKey;

    @Column(name = "role_name", nullable = false, unique = true)
    @QField(name = "角色名称", actions = {Action.show, Action.query, Action.edit}, queryType = QFieldQueryType.like, nullable = false)
    private String roleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_level")
    @QField(name = "数据权限", actions = {Action.show, Action.query}, queryType = QFieldQueryType.like, nullable = false)
    private DataLevel dataLevel;

    @Column(name = "device_types", columnDefinition = "text")
    @QField(name = "可控制的器件类型")
    private String deviceTypes;//可控制的器件类型

    @Transient
    private String dataLevelStr;
    @Transient
    private String[] selMenuIds;
    @Transient
    private String[] selSelResourceIds;

    public Role() {
    }

    public Role(String roleKey, String roleName) {
        this.roleKey = roleKey;
        this.roleName = roleName;
    }

    public String getDataLevelStr() {
        if (this.getDataLevel() != null) {
            return this.getDataLevel().getName();
        }
        return dataLevelStr;
    }

    public void setDataLevelStr(String dataLevelStr) {
        this.dataLevelStr = dataLevelStr;
    }
}
