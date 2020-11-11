package com.forpleuvoir.suikalib.config.annotation;

import java.lang.annotation.*;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.config.annotation
 * @class_name ConfigUpdateCallback
 * @create_time 2020/11/11 14:39
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConfigUpdateCallback {
}
