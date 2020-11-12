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
 * @class_name TestConfig
 * @create_time 2020/11/9 2:14
 */

@Config("test")
public class TestConfig {

    @ConfigUpdateCallback
    private UpdateCallback updateCallback;

    @ConfigField(value = "test_enabled", defValue = "true", type = FieldType.BOOLEAN)
    private boolean enabled;

    @ConfigField(value = "test_count", defValue = "66", type = FieldType.INTEGER)
    private Integer count;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled=enabled;
        onUpdate();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
        onUpdate();
    }

    private void onUpdate(){
        updateCallback.onUpdate(this);
    }
}
