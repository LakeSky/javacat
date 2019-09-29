package com.kzh.sys.service.generate.auto;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: kzh
 * Date: 13-10-17
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface QField {
    String name() default "";

    QFieldType type() default QFieldType.text;

    Action[] actions() default Action.none;

    boolean nullable() default true;

    //------------------------------------------
    String format() default "yyyy-MM-dd"; //"yyyy-MM-dd HH:mm:ss"

    QFieldDictType dictType() default QFieldDictType.common;

    String dictValues() default "";

    String dictSql() default "";

    QFieldQueryType queryType() default QFieldQueryType.eq;

    int textareaLength() default 500;
}
