package com.forpleuvoir.suikalib.reflection;

import java.io.IOException;
import java.util.List;

/**
 * @author forpleuvoir
 * @project_name suikalib
 * @package com.forpleuvoir.suikalib.Reflection
 * @class_name PackageScanner
 * @create_time 2020/11/9 1:26
 */

public interface PackageScanner {
    List<String> getFullyQualifiedClassNameList() throws IOException;
}
