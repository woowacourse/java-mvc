package com.interface21.webmvc.servlet.mvc.tobe.mapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.mvc.asis.Controller;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    private final Properties properties;

    public ManualHandlerMapping(Properties properties) {
        this.properties = properties;
    }

    public void register(String path, Controller controller) {
        controllers.put(path, controller);
        log.info("Registered Controller: {} -> {}", path, controller.getClass().getName());
    }

    @Override
    public void initialize() {
        System.out.println("Initializing ManualHandlerMapping with properties: " + properties);
        this.properties.forEach((key, value) -> {
            String propertyKey = key.toString();
            String propertyValue = value.toString();
            if (propertyKey.startsWith("controller./")) {
                String path = propertyKey.substring("controller.".length());
                try {
                    Class<?> clazz = Class.forName(propertyValue, true, Thread.currentThread().getContextClassLoader());
                    Controller controller = (Controller) clazz.getDeclaredConstructor().newInstance();
                    register(path, controller);
                } catch (Exception e) {
                    throw new RuntimeException("컨트롤러 등록 실패: " + propertyValue, e);
                }
            }
        });
    }


    @Override
    public Object getHandler(final HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        log.debug("수동 매핑 대상: {}", controllers.keySet());
        return controllers.get(requestURI);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
