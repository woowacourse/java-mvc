package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");

        try {
            final List<Class<?>> controllers = new ArrayList<>();
            for (Object path : basePackage) {
                findControllers((String) path, controllers);
            }
            for (Class<?> controller : controllers) {
                addHandler(controller);
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void findControllers(final String path, final List<Class<?>> controllers) throws URISyntaxException, IOException {
        final File[] files = findFilesByPath(path);

        for (File file : files) {
            final String filePath = path + "." + file.getName();
            if (file.isDirectory()) {
                findControllers(filePath, controllers);
            }
            if (file.getName().endsWith(".class")) {
                verifyController(filePath, controllers);
            }
        }
    }

    private static File[] findFilesByPath(final String path) throws URISyntaxException {
        final String packagePath = path.replace('.', '/');
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final File directory = new File(classLoader.getResource(packagePath).toURI());
        return directory.listFiles();
    }

    private void verifyController(final String file, final List<Class<?>> controllers) {
        try {
            final Class<?> clazz = convertToClass(file);
            if (clazz.isAnnotationPresent(context.org.springframework.stereotype.Controller.class)) {
                controllers.add(clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Class<?> convertToClass(final String file) throws ClassNotFoundException {
        String className = file.replace(".class", "");
        return Class.forName(className);
    }

    private void addHandler(final Class<?> controller) {
        for (Method method : controller.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                for (RequestMethod requestMethod : requestMapping.method()) {
                    addHandlerByMethod(controller, method, requestMapping, requestMethod);
                }
            }
        }
    }

    private void addHandlerByMethod(
            final Class<?> controller,
            final Method method,
            final RequestMapping requestMapping,
            final RequestMethod requestMethod
    ) {
        final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
        final Object target;
        try {
            target = controller.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        final HandlerExecution handlerExecution = new HandlerExecution(target, method);

        handlerExecutions.put(handlerKey, handlerExecution);
    }

    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
