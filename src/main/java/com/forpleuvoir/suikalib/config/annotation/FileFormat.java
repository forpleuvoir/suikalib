package com.forpleuvoir.suikalib.config.annotation;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.config.annotation
 * @class_name FileFormat
 * @create_time 2020/11/9 0:47
 */

public enum FileFormat {
    JSON(".json");

    private final String value;

    FileFormat(String value) {
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
