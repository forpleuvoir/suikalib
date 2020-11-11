package com.forpleuvoir.suikalib.config.annotation;

import com.forpleuvoir.suikalib.config.FieldType;

import java.lang.annotation.*;

/**
 * 配置属性
 *
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.config.annotation
 * @class_name ConfigField
 * @create_time 2020/11/10 18:39
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConfigField {

    /**
     * 保存的key值 为空则默认使用属性名
     *
     * @return
     */
    String value() default "";

    String defValue();

    FieldType type();
}
