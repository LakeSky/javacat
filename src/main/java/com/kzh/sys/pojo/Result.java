package com.kzh.sys.pojo;

import com.kzh.sys.service.generate.auto.QField;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    @QField(name = "是否成功")
    private boolean success;
    @QField(name = "返回消息")
    private String msg;
    @QField(name = "返回数据")
    private Object data;
    @QField(name = "code")
    private String code;

    private Integer errno = 0;

    private String url;

    public Result() {
        this.success = false;
    }

    public Result(Object data) {
        this.data = data;
    }

    public Result(boolean success) {
        this.success = success;
        msg = "";
    }

    public Result(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public Result(boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public Result(Object data, boolean success) {
        this.success = success;
        this.data = data;
    }
}
