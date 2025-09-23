package com.interface21.webmvc.servlet.mvc.tobe.util;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties load(ServletContext context, String path) {
        InputStream inputStream = context.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalStateException(path + " 파일을 찾을 수 없습니다.");
        }
        try (inputStream) {
            Properties props = new Properties();
            props.load(inputStream);
            return props;
        } catch (IOException e) {
            throw new RuntimeException(path + " 로드 실패", e);
        }
    }
}
