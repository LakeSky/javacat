package com.kzh.sys.model;

import com.kzh.sys.enums.DictKey;
import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldQueryType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by gang on 2018/4/13.
 */
@QClass(name = "数据字典")
@Entity
@Table(name = "sys_dict")
@Data
@EqualsAndHashCode(callSuper = false)
public class Dict extends BaseEntity {
    @QField(name = "所在公司", actions = {Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "company_id", columnDefinition = "varchar(100) COMMENT '公司id'")
    private String companyId;//公司id

    @QField(name = "键", actions = {Action.edit, Action.query, Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "dict_key", columnDefinition = "varchar(100) COMMENT '键'")
    @Enumerated(EnumType.STRING)
    private DictKey dictKey;//键

    @QField(name = "值", actions = {Action.edit, Action.query, Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "dict_value", columnDefinition = "varchar(100) COMMENT '值'")
    private String dictValue;//值

    @QField(name = "备注", actions = {Action.edit, Action.query, Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "bz", columnDefinition = "varchar(255) COMMENT '备注'")
    private String bz;//备注

    @Transient
    @QField(name = "键", actions = {Action.show, Action.export})
    private String dictKeyStr;
}
