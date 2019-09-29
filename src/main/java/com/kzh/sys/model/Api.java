package com.kzh.sys.model;

import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldQueryType;
import com.kzh.sys.service.generate.auto.QFieldType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by gang on 2018/2/4.
 */
@Entity
@Table(name = "sys_api")
@Data
@EqualsAndHashCode(callSuper = false)
public class Api extends BaseEntity {
    @QField(name = "url", actions = {Action.edit, Action.query, Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "url", unique = true)
    private String url;

    @QField(name = "请求示例", type = QFieldType.textarea, actions = {Action.edit, Action.show})
    @Column(name = "req_example", columnDefinition = "text")
    private String reqExample;

    @QField(name = "返回示例", type = QFieldType.textarea, actions = {Action.edit, Action.show})
    @Column(name = "res_example", columnDefinition = "text")
    private String resExample;

    @QField(name = "备注", type = QFieldType.textarea, actions = {Action.edit, Action.show})
    @Column(name = "bz", columnDefinition = "text")
    private String bz;
}
