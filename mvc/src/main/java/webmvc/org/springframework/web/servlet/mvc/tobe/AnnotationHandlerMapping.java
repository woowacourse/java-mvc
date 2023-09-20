package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.HandlerMapping;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        try {
            initializeHandlers();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeHandlers() throws URISyntaxException, IOException {
        final List<Class<?>> controllers = new ArrayList<>();
        for (Object path : basePackage) {
            findControllers((String) path, controllers);
        }
        for (Class<?> controller : controllers) {
            addHandler(controller);
        }
    }

    private void findControllers(final String path, final List<Class<?>> controllers) throws URISyntaxException, IOException {
        final File[] files = findFilesByPath(path);

        for (File file : files) {
            findControllerByFile(path, controllers, file);
        }
    }

    private static File[] findFilesByPath(final String path) throws URISyntaxException {
        final String packagePath = path.replace('.', '/');
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final File directory = new File(classLoader.getResource(packagePath).toURI());
        return directory.listFiles();
    }

    private void findControllerByFile(final String path, final List<Class<?>> controllers, final File file)
            throws URISyntaxException, IOException {
        final String filePath = path + "." + file.getName();
        if (file.isDirectory()) {
            findControllers(filePath, controllers);
        }
        if (file.getName().endsWith(".class")) {
            verifyController(filePath, controllers);
        }
    }

    private void verifyController(final String file, final List<Class<?>> controllers) {
        try {
            final Class<?> clazz = convertToClass(file);
            addControllerClass(controllers, clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Class<?> convertToClass(final String file) throws ClassNotFoundException {
        String className = file.replace(".class", "");
        return Class.forName(className);
    }

    private static void addControllerClass(final List<Class<?>> controllers, final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Controller.class)) {
            controllers.add(clazz);
        }
    }

    private void addHandler(final Class<?> controller) {
        Arrays.stream(controller.getDeclaredMethods())
                .forEach(this::addHandlerByMethod);
    }

    private void addHandlerByMethod(final Method method) {
        if (method.isAnnotationPresent(RequestMapping.class)) {
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            addHandlerByMethod(method, requestMapping);
        }
    }

    private void addHandlerByMethod(final Method method, final RequestMapping requestMapping) {
        for (RequestMethod requestMethod : requestMapping.method()) {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(handlerKey, handlerExecution);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
