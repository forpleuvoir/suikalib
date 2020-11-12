package com.forpleuvoir.suikalib.config;

import com.forpleuvoir.suikalib.config.annotation.ConfigUpdateCallback;
import com.forpleuvoir.suikalib.config.configInterface.UpdateCallback;
import com.forpleuvoir.suikalib.reflection.ClassScanner;
import com.forpleuvoir.suikalib.reflection.ClasspathPackageScanner;
import com.forpleuvoir.suikalib.reflection.PackageScanner;
import com.forpleuvoir.suikalib.reflection.ReflectionUtil;
import com.forpleuvoir.suikalib.config.annotation.Config;
import com.forpleuvoir.suikalib.config.annotation.ConfigField;
import com.forpleuvoir.suikalib.config.annotation.SuikaConfig;
import com.forpleuvoir.suikalib.util.FileUtil;
import com.forpleuvoir.suikalib.util.JsonUtil;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 配置主类
 *
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.config
 * @class_name SuikaConfig
 * @create_time 2020/11/8 16:24
 */

public class SuikaConfigApp implements UpdateCallback {

    private static String fileName;
    private static String filePath;
    private static File configFile;
    private static final Set<Class<?>> configClass = new HashSet<>();
    private static final Map<String, Map<String, Object>> configObject = new HashMap<>();
    private static final Map<String, Object> configBean = new HashMap<>();

    public static void init(Class<?> configClass) {
        SuikaConfig config = configClass.getAnnotation(SuikaConfig.class);
        scanConfigClass(config.packages());
        loadConfig(configClass, config);
        injectBean(configClass);
    }

    public static void init(Class<?> config,Set<Class<?>> clazz) {
        SuikaConfig configs = config.getAnnotation(SuikaConfig.class);
        configClass.addAll(clazz);
        loadConfig(config, configs);
        injectBean(config);
    }

    private static void injectBean(Class<?> configClass) {
        scanConfigBean(configClass).forEach(field -> {
            field.setAccessible(true);
            Config config = field.getAnnotation(Config.class);
            assert config != null;
            String key = config.value().isEmpty() ? field.getName() : config.value();
            try {
                field.set(null, configBean.get(key));
                injectCallback(configBean.get(key));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    private static void injectCallback(Object object) {
        if (object != null)
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                ConfigUpdateCallback config = field.getAnnotation(ConfigUpdateCallback.class);
                if (config != null) {
                    try {
                        field.set(object, new SuikaConfigApp());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    /**
     * 加载配置文件
     *
     * @param configClass 配置类
     * @param config      注解类
     */
    private static void loadConfig(Class<?> configClass, SuikaConfig config) {
        if (!checkConfig(configClass, config)) {
            try {
                createConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
            initConfigFile();
            loadConfig(configClass, config);
        }
        readConfigFile();
    }

    private static void readConfigFile() {
        AtomicBoolean isChanged = new AtomicBoolean(false);
        try {
            Map<String, Map<String, Object>> map = JsonUtil.fromJson(JsonParser.parseReader(new FileReader(configFile)), new TypeToken<Map<String, Map<String, Object>>>() {
            }.getType());
            map.forEach(configObject::put);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        configClass.forEach(clazz -> {
            String name = clazz.getAnnotation(Config.class).value();
            Object bean = null;
            try {
                bean = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (name.isEmpty()) {
                name = clazz.getSimpleName();
            }
            Map<String, Object> map;
            if (!configObject.containsKey(name)) {
                Map<String, Object> aMaps = new HashMap<>();
                ReflectionUtil.getFieldByAnnotation(clazz, ConfigField.class).forEach(field -> {
                    ConfigField configField = field.getAnnotation(ConfigField.class);
                    String n = configField.value().isEmpty() ? field.getName() : configField.value();
                    aMaps.put(n, FieldType.getDefValue(configField));
                });
                configObject.put(name, aMaps);
                isChanged.set(true);
            }
            map = configObject.get(name);
            Object finalObj = bean;
            ReflectionUtil.getFieldByAnnotation(clazz, ConfigField.class).forEach(field -> {
                ConfigField configField = field.getAnnotation(ConfigField.class);
                String n = configField.value().isEmpty() ? field.getName() : configField.value();
                assert finalObj != null;
                if (!map.containsKey(n)) {
                    Object defValue = FieldType.getDefValue(configField);
                    map.put(n, defValue);
                    isChanged.set(true);
                }
                ReflectionUtil.setFieldValueByName(field.getName(), finalObj, map.get(n));
            });
            configBean.put(name, finalObj);
        });
        if (isChanged.get()) {
            saveConfig();
        }
    }

    /**
     * 检查配置文件是否存在
     *
     * @param configClass 配置主类对象
     * @param config      配置主类注解对象
     * @return boolean
     */
    private static boolean checkConfig(Class<?> configClass, SuikaConfig config) {
        filePath = config.path();
        if (filePath.isEmpty()) {
            try {
                filePath = new File("").getCanonicalPath() + "/" + configClass.getSimpleName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileName = config.filename();
        if (fileName.isEmpty()) {
            fileName = configClass.getSimpleName();
        }
        fileName = fileName + config.format().getValue();
        configFile = new File(filePath + "/" + fileName);
        return FileUtil.checkFile(configFile);
    }

    private static void createConfig() throws IOException {
        configFile = FileUtil.createFile(filePath, fileName);
    }

    private static void saveConfig() {
        String content = String.valueOf(JsonUtil.toStringBuffer(configObject));
        try {
            FileUtil.writeFile(configFile, content, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化配置文件
     */
    private static void initConfigFile() {
        configClass.forEach(c -> {
            String name = c.getAnnotation(Config.class).value();
            Object obj = null;
            try {
                obj = c.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if (name.isEmpty()) {
                name = c.getSimpleName();
            }
            Map<String, Object> fields = new HashMap<>();
            Object finalObj = obj;
            ReflectionUtil.getFieldByAnnotation(c, ConfigField.class).forEach(field -> {
                ConfigField configField = field.getAnnotation(ConfigField.class);
                String n = configField.value();
                if (n.isEmpty()) {
                    n = field.getName();
                }
                Object defValue = FieldType.getDefValue(configField);
                assert finalObj != null;
                ReflectionUtil.setFieldValueByName(field.getName(), finalObj, defValue);
                fields.put(n, defValue);
            });
            configObject.put(name, fields);
            configBean.put(name, finalObj);

        });
        saveConfig();
    }


    private static Set<Field> scanConfigBean(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        for (Field field : clazz.getDeclaredFields()) {
            Config config = field.getAnnotation(Config.class);
            if (config != null) {
                fields.add(field);
            }
        }
        return fields;
    }


    /**
     * 扫描config类
     *
     * @param packName 包名数组
     */
    private static void scanConfigClass(String[] packName) {
        List<Class<?>> classList = new ArrayList<>();
        try {
            classList = ClassScanner.searchClass(packName);
        } catch (Exception e) {
            try {
                classList = ClassScanner.searchClassForJar(packName);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            e.printStackTrace();
        }
        if (!classList.isEmpty())
            classList.forEach(clazz -> {
                if (clazz.getAnnotation(Config.class) != null) {
                    configClass.add(clazz);
                }
            });

//        for (String s : packName) {
//            PackageScanner scan = new ClasspathPackageScanner(s);
//            List<String> list = null;
//
//            if (list != null)
//                list.forEach(str -> {
//                    try {
//                        Class<?> clazz = Class.forName(str);
//                        if (clazz.getAnnotation(Config.class) != null) {
//                            configClass.add(clazz);
//                        }
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                });
//        }

    }

    private static Map<String, Object> beanToMap(Object bean) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            ConfigField configField = field.getAnnotation(ConfigField.class);
            field.setAccessible(true);
            if (configField != null) {
                String key = configField.value().isEmpty() ? field.getName() : configField.value();
                try {
                    map.put(key, field.get(bean));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 将对象同步到键值对数据中
     */
    private static void synchronize() {
        configBean.forEach((k, v) -> configObject.put(k, beanToMap(v)));
    }

    @Override
    public void onUpdate(Object o) {
        synchronize();
        saveConfig();
    }
}
