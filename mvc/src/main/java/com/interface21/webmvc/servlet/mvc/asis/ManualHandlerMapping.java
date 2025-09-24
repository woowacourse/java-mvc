package com.interface21.webmvc.servlet.mvc.asis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);
    private static final Map<String, Controller> CONTROLLER_MAPPING = new HashMap<>();
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String RESOURCE_NAME = "controller_mapping.json";

    private final Object[] basePackage;

    public ManualHandlerMapping(String basePackage, String... others) {
        this.basePackage = Stream.concat(Stream.of(basePackage), Arrays.stream(others)).toArray(String[]::new);
    }

    @Override
    public void initialize() {
        final var reflections = new Reflections(basePackage);
        Map<String, Controller> controllerNameAndClasses = reflections.get(Scanners.SubTypes.of(Controller.class).asClass()).stream()
                .collect(Collectors.toMap(
                        Class::getSimpleName,
                        c -> {
                            try {
                                Object instance = c.getDeclaredConstructor().newInstance();
                                if (instance instanceof Controller controller) return controller;
                                else throw new IllegalStateException();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));

        List<ControllerMapping> controllerMappings = loadControllerMappings();
        controllerMappings.forEach(cm -> CONTROLLER_MAPPING.put(cm.url(), controllerNameAndClasses.get(cm.className())));

        log.info("Initialized ManualHandlerMapping!");
        CONTROLLER_MAPPING.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, CONTROLLER_MAPPING.get(path).getClass()));
    }

    private static List<ControllerMapping> loadControllerMappings() {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try (InputStream is = cl.getResourceAsStream(RESOURCE_NAME)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found on classpath " + RESOURCE_NAME);
            }
            return MAPPER.readValue(is, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to load/parse " + RESOURCE_NAME, e);
        }
    }

    private record ControllerMapping(
            String url,
            String className
    ) {

    }

    @Override
    public Controller getHandler(final HttpServletRequest request) {
        log.debug("Request Mapping Uri : {}", request.getRequestURI());
        return CONTROLLER_MAPPING.get(request.getRequestURI());
    }
}
