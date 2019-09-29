package com.kzh.sys.service.generate.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * User: kzh
 * Date: 13-10-17
 */
@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface QClass {
    String name() default "";

    QClassAction[] actions() default QClassAction.none;
}
