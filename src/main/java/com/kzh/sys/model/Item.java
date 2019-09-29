package com.kzh.sys.model;

import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldQueryType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by gang on 2018/6/30.
 */

@Data
@EqualsAndHashCode(callSuper = false)
@QClass(name = "属性项")
@Entity
@Table(name = "b_item")
public class Item extends BaseEntity {
    @QField(name = "名称", actions = {Action.edit, Action.query, Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "name", columnDefinition = "varchar(100) COMMENT '名称'")
    private String name;//名称

    @QField(name = "值", actions = {Action.edit, Action.query, Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "value", columnDefinition = "varchar(500) COMMENT '值'")
    private String value;//值

    @Column(name = "obj_id")
    private String objId;

    @Transient
    private String propertyName;

    //==================== set get 构造方法 ====================

}

