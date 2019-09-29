package com.kzh.sys.service.generate.auto;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: kzh
 * Date: 13-10-17
 * 在Controller里面定义页面打开的时候左侧点亮的关联菜单
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface QMapping {
    String name() default "";

    String relatedUrl() default "";
}
