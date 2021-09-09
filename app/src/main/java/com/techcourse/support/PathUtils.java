package com.techcourse.support;

import java.io.File;

public class PathUtils {

    private PathUtils() {}

    public static String getWebAppPath() {
        return addRightWebAppPath(new File("").getAbsolutePath());
    }

    static String addRightWebAppPath(String absolutePath) {
        if (absolutePath.endsWith("/app")) {
            return absolutePath + "/webapp";
        }

        return absolutePath + "/app/webapp";
    }
}
