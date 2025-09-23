package com.interface21.webmvc.servlet.mvc.tobe.util;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    public static Properties load(ServletContext context, String path) {
        try (InputStream in = context.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException(path + " 파일을 찾을 수 없습니다.");
            }
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new RuntimeException(path + " 로드 실패", e);
        }
    }
}
