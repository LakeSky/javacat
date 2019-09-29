package com.kzh.sys.model;

import com.kzh.sys.service.generate.auto.QClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 菜单
 */
@QClass(name = "菜单")
@Entity
@Table(name = "sys_menu")
@Data
@EqualsAndHashCode(callSuper = false)
public class Menu extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_id", nullable = false)
    private String parentId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "seq")
    private Integer seq;

    @Column(name = "icon")
    private String icon;
}
