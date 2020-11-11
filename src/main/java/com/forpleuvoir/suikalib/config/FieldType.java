package com.forpleuvoir.suikalib.config;

import com.forpleuvoir.suikalib.config.annotation.ConfigField;

/**
 * 字段类型
 *
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.config
 * @class_name FieldType
 * @create_time 2020/11/10 19:54
 */

public enum FieldType {
    BOOLEAN(Boolean.class),
    STRING(String.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    SHORT(Short.class),
    CHAR(Character.class),
    DOUBLE(Double.class),
    FLOAT(Float.class);

    private Class<?> clazz;

    FieldType(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static Object getDefValue(ConfigField configField) {
        String strValue = configField.defValue();
        switch (configField.type()) {
            case BOOLEAN:
                return Boolean.parseBoolean(strValue);
            case CHAR:
                return strValue.charAt(0);
            case LONG:
                return Long.parseLong(strValue);
            case FLOAT:
                return Float.parseFloat(strValue);
            case SHORT:
                return Short.parseShort(strValue);
            case DOUBLE:
                return Double.parseDouble(strValue);
            case STRING:
                return strValue;
            case INTEGER:
                return Integer.parseInt(strValue);

        }
        return "";
    }
}
