package com.kzh.sys.pojo.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by gang on 2018/2/1.
 */
//接口字段模型
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiInterface {
    private String name;//名字
    private String address;//接口地址
    private String returnType;//返回格式
    private String requestType;//请求方式
    private String desc;//接口描述
    private List<ApiReqParameter> apiReqParameters;//请求参数
    private List<ApiResParameter> apiResParameters;//返回参数
    private String reqExample;//请求示例
    private String resExample;//返回示例
    private String bz;
}
