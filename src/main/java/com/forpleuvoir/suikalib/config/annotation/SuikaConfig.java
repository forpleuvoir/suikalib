package com.forpleuvoir.suikalib.config.annotation;

import java.lang.annotation.*;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.config.annotation
 * @class_name SuikaConfig
 * @create_time 2020/11/8 23:26
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SuikaConfig {
    /**
     *  app id
     * @return
     */
    String value() default "";

    /**
     * 文件保存路径
     * @return
     */
    String path() default "";

    /**
     * 保存的文件名
     * @return
     */
    String filename() default "";

    /**
     * 文件格式
     * @return
     */
    FileFormat format() default FileFormat.JSON;

    /**
     * 需要扫描扫描的包
     *
     * @return
     */
    String[] packages() default "";

}
