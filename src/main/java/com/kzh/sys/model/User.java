package com.kzh.sys.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldQueryType;
import com.kzh.sys.util.qiniu.UploadUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@QClass(name = "用户表")
@Entity
@Table(name = "sys_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity implements UserDetails {
    @Column(unique = true)
    @QField(name = "用户名", actions = {Action.show, Action.query, Action.edit}, queryType = QFieldQueryType.like, nullable = false)
    private String username;

    @QField(name = "手机号", actions = {Action.show, Action.edit})
    private String phone;

    @QField(name = "姓名", actions = {Action.show, Action.query, Action.edit}, queryType = QFieldQueryType.like, nullable = false)
    @Column(name = "name")
    private String name;

    @QField(name = "密码", actions = {Action.edit})
    @JsonIgnore
    private String password;

    @QField(name = "头像")
    @Column(name = "avatar")
    private String avatar;

    @QField(name = "openid")
    @Column(name = "openid")
    private String openid;

    @QField(name = "阅读消息")
    @Column(name = "read_msg_time")
    private Date readMsgTime;

    @Transient
    private Collection<GrantedAuthority> authorities;

    //Spring Security 会判断这几个值，如非必要，不要修改
    private Boolean accountNonExpired = true;
    private Boolean accountNonLocked = true;
    private Boolean credentialsNonExpired = true;
    private Boolean enabled = true;

    @QField(name = "角色", nullable = false)
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    public User() {
    }

    public User(String username, String password, String phone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    //==================== set get 方法 ====================
    public String getAvatar() {
        return UploadUtil.getFullDownLoadUrlByFileId(avatar);
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}


