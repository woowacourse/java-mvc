package com.techcourse.support;

import java.io.File;

public class PathUtils {

    private PathUtils() {}

    public static String getWebAppPath() {
        final String absolutePath = new File("").getAbsolutePath();
        if (absolutePath.endsWith("/app")) {
            return absolutePath + "/webapp";
        }

        return absolutePath + "/app/webapp";
    }
}
