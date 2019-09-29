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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang on 2017/3/12.
 */
//操作日志
@QClass(name = "系统日志")
@Entity
@Table(name = "sys_log")
@Data
@EqualsAndHashCode(callSuper = false)
public class Log extends BaseEntity {
    @Column(name = "obj_id", nullable = false)
    @QField(name = "对象id")
    private String objId;//对象id

    @Column(name = "category", nullable = false)
    @QField(name = "类别")
    private String category;//类别

    @Column(name = "action", nullable = false)
    @QField(name = "动作")
    private String action;//动作

    @Column(name = "content", columnDefinition = "text", nullable = false)
    @QField(name = "处理内容", actions = {Action.edit, Action.query, Action.show}, queryType = QFieldQueryType.like, nullable = false)
    private String content;//内容

    @Column(name = "detail", columnDefinition = "text")
    @QField(name = "详细")
    private String detail;//详细

    public Log() {

    }

    public Log(String objId, String category, String action, String content, String detail) {
        this.objId = objId;
        this.category = category;
        this.action = action;
        this.content = content;
        this.detail = detail;
    }

    @Transient
    private List<String> files = new ArrayList<>();
}
