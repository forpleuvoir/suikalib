package com.forpleuvoir.test;

import com.forpleuvoir.suikalib.config.SuikaConfigApp;
import com.forpleuvoir.suikalib.config.annotation.Config;
import com.forpleuvoir.suikalib.config.annotation.SuikaConfig;
import com.forpleuvoir.test.config.TestConfig;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir
 * @class_name ConfigTest
 * @create_time 2020/11/11 19:18
 */

@SuikaConfig(value = "suika", packages = "com.forpleuvoir.test.config")
public class ConfigTest {

    @Config("test")
    public static TestConfig testConfig;

    public static void main(String[] args) {
        SuikaConfigApp.init(ConfigTest.class);
    }
}
