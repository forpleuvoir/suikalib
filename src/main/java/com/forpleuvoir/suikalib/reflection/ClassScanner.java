package com.forpleuvoir.suikalib.reflection;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.reflection
 * @class_name ClassScanner
 * @create_time 2020/11/11 22:50
 */

public class ClassScanner {

    private static List<String> classPaths = new ArrayList<>();

    /**
     * 扫描指定包下的所有类
     *
     * @param basePackage
     * @return
     * @throws Exception
     */
    public static List<Class<?>> searchClassForJar(String[] basePackage) throws Exception {
        List<Class<?>> list = new ArrayList<>();
        for (String s : basePackage) {
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(s.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();
                if ("jar".equalsIgnoreCase(protocol)) {
                    JarURLConnection connection = (JarURLConnection) url.openConnection();
                    if (connection != null) {
                        JarFile jarFile = connection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {
                                JarEntry entry = jarEntryEnumeration.nextElement();
                                String jarEntryName = entry.getName();
                                if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/", ".").startsWith(s)) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                    list.add(Class.forName(className));
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    public static List<Class<?>> searchClass(String[] basePackage) throws Exception {
        List<Class<?>> list = new ArrayList<>();
        for (String s : basePackage) {
            String classPath = ClassScanner.class.getResource("/").getPath();
            String str=s;
            s = s.replace(".", File.separator);
            String searchPath = classPath + s;
            doPath(new File(searchPath));
            for (String path : classPaths) {
                path = path.replace(classPath.replace("/", "\\").replaceFirst("\\\\", ""), "").replace("\\", ".").replace(".class", "");
                list.add(Class.forName(str+"."+path));
            }
        }

        return list;
    }

    private static void doPath(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            assert files != null;
            for (File file1 : files) {
                doPath(file1);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                classPaths.add(file.getName());
            }
        }
    }
}
