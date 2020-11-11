package com.forpleuvoir.suikalib.config.annotation;

import java.lang.annotation.*;

/**
 * 配置注解
 *
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.config.annotation
 * @class_name Config
 * @create_time 2020/11/8 20:39
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Config {

    /**
     * 配置的key
     *
     * @return
     */
    String value() default "";

}
