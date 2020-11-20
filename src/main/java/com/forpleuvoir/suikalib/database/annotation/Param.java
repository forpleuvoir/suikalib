package com.forpleuvoir.suikalib.database.annotation;

import java.lang.annotation.*;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.database.annotation
 * @class_name parm
 * @create_time 2020/11/15 11:07
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Param {
    String value() default "";
}
