package com.forpleuvoir.test.config;

import com.forpleuvoir.suikalib.config.FieldType;
import com.forpleuvoir.suikalib.config.annotation.Config;
import com.forpleuvoir.suikalib.config.annotation.ConfigField;
import com.forpleuvoir.suikalib.config.annotation.ConfigUpdateCallback;
import com.forpleuvoir.suikalib.config.configInterface.UpdateCallback;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.test.config
 * @class_name TestConfig2
 * @create_time 2020/11/11 12:31
 */

@Config("test2")
public class TestConfig2 {

    @ConfigUpdateCallback
    private UpdateCallback updateCallback;

    @ConfigField(value = "t1",defValue = "123",type= FieldType.INTEGER)
    private Integer s;

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
        updateCallback.onUpdate(this);
    }
}
