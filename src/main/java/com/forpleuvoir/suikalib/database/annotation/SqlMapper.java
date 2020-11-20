package com.forpleuvoir.suikalib.database.annotation;

import java.lang.annotation.*;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.database.annotation
 * @class_name Mapper
 * @create_time 2020/11/15 11:17
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SqlMapper {
    String value();
}
