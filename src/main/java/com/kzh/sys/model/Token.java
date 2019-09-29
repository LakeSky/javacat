package com.kzh.sys.model;

import com.kzh.sys.service.generate.auto.QClass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by gang on 2016/7/31.
 * 接口token
 */
@QClass(name = "接口token")
@Entity
@Table(name = "sys_token")
@Data
@EqualsAndHashCode(callSuper = false)
public class Token extends BaseEntity {
    //平台发行方,可以解决不同平台登陆的token分离问题
    @Column(name = "issuer")
    private String issuer;
    @Column(name = "token_key")
    private String tokenKey;
    @Column(name = "token_value")
    private String tokenValue;
}
