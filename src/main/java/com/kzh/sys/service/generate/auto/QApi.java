package com.kzh.sys.service.generate.auto;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: kzh
 * 定义Api的返回数据的类型
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface QApi {
    Class dataClass();
}
