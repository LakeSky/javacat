package com.kzh.sys.model;


import com.kzh.sys.service.generate.auto.QClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统配置
 */
@QClass(name = "系统配置")
@Entity
@Table(name = "sys_config")
@Data
@EqualsAndHashCode(callSuper = false)
public class Config extends BaseEntity {

    @Column(name = "config_key", length = 50)
    private String configKey;
    @Column(name = "config_value", columnDefinition = "text")
    private String configValue;
    @Column(name = "description", length = 200)
    private String description;
    @Column(name = "status", length = 20)
    private String status;
    
    public Config() {
    }

    public Config(String configKey, String configValue, String description, String status) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.description = description;
        this.status = status;
    }
}