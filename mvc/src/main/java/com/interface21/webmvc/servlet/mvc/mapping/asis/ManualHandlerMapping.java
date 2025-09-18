package com.interface21.webmvc.servlet.mvc.mapping.asis;

import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final String MAPPINGS_PREFIX = "mappings.";

    private final Map<String, Controller> controllers = new HashMap<>();
    private final Properties properties;

    public ManualHandlerMapping(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void initialize() {
        properties.forEach((key, value) -> {
            String keyString = (String) key;
            String className = (String) value;

            if (!keyString.startsWith(MAPPINGS_PREFIX)) {
                log.debug("Skip unrelated property: {}", keyString);
                return;
            }

            loadControllerFromProperty(keyString, className);
        });

        log.info("Initialized Handler Mapping!");
        controllers.forEach((path, controller) ->
                log.info("Path: {}, Controller: {}", path, controller.getClass()));
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }

    private void loadControllerFromProperty(String key, String className) {
        String path = extractPathFromKey(key);
        Controller controller = instantiateController(className);
        controllers.put(path, controller);
    }

    private String extractPathFromKey(String key) {
        // ex) "mappings./login" -> "/login"
        return key.substring(MAPPINGS_PREFIX.length());
    }

    private Controller instantiateController(String className) {
        try {
            // 클래스로더 불일치 이슈로 Thread.currentThread().getContextClassLoader() 파라미터 추가
            // [참고] https://melodic-walleye-2ab.notion.site/2725051bdea5807fb8d2cc5367cdda49?source=copy_link
            Class<?> clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
            return (Controller) clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate controller: " + className, e);
        }
    }
}
