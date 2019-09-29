package com.kzh.busi.model;

import com.kzh.sys.model.BaseEntity;
import com.kzh.sys.service.generate.auto.*;
import com.kzh.sys.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@QClass(name = "员工")
@Entity
@Table(name = "b_employee")
@Data
@EqualsAndHashCode(callSuper = false)
public class Employee extends BaseEntity {
    @QField(name = "名字", actions = {Action.query, Action.edit, Action.show}, queryType = QFieldQueryType.like, nullable = false)
    @Column(name = "name", columnDefinition = "varchar(100) COMMENT '名字'")
    private String name;//名字

    @QField(name = "年龄", actions = {Action.edit, Action.show})
    @Column(name = "age", columnDefinition = "int COMMENT '年龄'")
    private Integer age;//年龄

    @QField(name = "生日", type = QFieldType.date, actions = {Action.query, Action.edit, Action.show}, queryType = QFieldQueryType.like)
    @Column(name = "birthday", columnDefinition = "datetime COMMENT '生日'")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;//生日

    @QField(name = "是否可用")
    @Column(name = "enabled", columnDefinition = "varchar(255) COMMENT '是否可用'")
    private Boolean enabled;//是否可用

    @QField(name = "个人简介", type = QFieldType.textarea, actions = {Action.edit})
    @Column(name = "description", columnDefinition = "text COMMENT '个人简介'")
    private String description;//个人简介

    //-------get/set------------------------------
    @Transient
    private String birthdayStr;
    @Transient
    private String birthday_start;
    @Transient
    private String birthday_end;

    public String getBirthdayStr() {
        if (this.getBirthday() != null) {
            return DateUtil.format("yyyy-MM-dd", this.getBirthday());
        }
        return birthdayStr;
    }
}