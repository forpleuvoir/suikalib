package com.forpleuvoir.suikalib.database.annotation;

import java.lang.annotation.*;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.database.annotation
 * @class_name Select
 * @create_time 2020/11/15 11:01
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Select {
    String value();
}
