package com.kzh.sys.model;

import com.kzh.sys.enums.ReadState;
import com.kzh.sys.service.generate.auto.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by gang on 2017/4/25.
 */
//系统消息
@QClass(name = "系统消息")
@Entity
@Table(name = "sys_msg")
@Data
@EqualsAndHashCode(callSuper = false)
public class Msg extends BaseEntity {
    @Column(name = "obj_id", nullable = false)
    @QField(name = "对象id", actions = {Action.edit}, nullable = false)
    private String objId;//对象id

    @Column(name = "title", nullable = false)
    @QField(name = "标题", actions = {Action.edit}, nullable = false)
    private String title;//标题

    @Column(name = "handle_action", nullable = false)
    @QField(name = "处理动作", actions = {Action.edit}, nullable = false)
    private String handleAction;//动作

    @Column(name = "content", columnDefinition = "text", nullable = false)
    @QField(name = "处理内容", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like, nullable = false)
    private String content;//内容

    @Column(name = "username", nullable = false)
    @QField(name = "通知用户", actions = {Action.edit, Action.show}, nullable = false)
    private String username;//通知用户

    @QField(name = "状态", type = QFieldType.dict, dictValues = "{'YD':'已读','WD':'未读'}", actions = {Action.edit, Action.query, Action.show})
    @Column(name = "read_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReadState readState;

    @Transient
    private String caseTitle;

    @Transient
    private String creatorName;

    @Transient
    private String noticeName;

    public Msg() {
    }

    public Msg(String objId, String title, String handleAction, String content, String username) {
        this.objId = objId;
        this.title = title;
        this.handleAction = handleAction;
        this.content = content;
        this.username = username;
        this.readState = ReadState.WD;
    }
}
