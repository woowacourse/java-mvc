package webmvc.org.springframework.web.servlet.mvc.tobe;

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
import webmvc.org.springframework.web.servlet.ExceptionHandler;
import webmvc.org.springframework.web.servlet.ExceptionHandlerMapping;
import webmvc.org.springframework.web.servlet.ExceptionManager;

public class AnnotationExceptionHandlerMapping implements ExceptionHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationExceptionHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<Class<? extends Throwable>, HandlerExecution> handlerExecutions;

    public AnnotationExceptionHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    @Override
    public void initialize() {
        log.info("Initialized AnnotationExceptionHandlerMapping!");

        try {
            initializeExceptionHandlers();
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeExceptionHandlers() throws URISyntaxException, IOException {
        final List<Class<?>> exceptionHandlers = new ArrayList<>();
        for (Object path : basePackage) {
            findExceptionHandlers((String) path, exceptionHandlers);
        }
        for (Class<?> controller : exceptionHandlers) {
            addHandler(controller);
        }
    }

    private void findExceptionHandlers(final String path, final List<Class<?>> controllers) throws URISyntaxException, IOException {
        final File[] files = findFilesByPath(path);

        for (File file : files) {
            findExceptionHandlersByFile(path, controllers, file);
        }
    }

    private static File[] findFilesByPath(final String path) throws URISyntaxException {
        final String packagePath = path.replace('.', '/');
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final File directory = new File(classLoader.getResource(packagePath).toURI());
        return directory.listFiles();
    }

    private void findExceptionHandlersByFile(final String path, final List<Class<?>> controllers, final File file)
            throws URISyntaxException, IOException {
        final String filePath = path + "/" + file.getName();
        if (file.isDirectory()) {
            findExceptionHandlers(filePath, controllers);
        }
        if (file.getName().endsWith(".class")) {
            verifyExceptionHandler(filePath, controllers);
        }
    }

    private void verifyExceptionHandler(final String file, final List<Class<?>> controllers) {
        try {
            final Class<?> clazz = convertToClass(file);
            addExceptionHandlerClass(controllers, clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Class<?> convertToClass(final String file) throws ClassNotFoundException {
        String className = file
                .replace(".class", "")
                .replace("/", ".");
        return Class.forName(className);
    }

    private static void addExceptionHandlerClass(final List<Class<?>> controllers, final Class<?> clazz) {
        if (clazz.isAnnotationPresent(ExceptionManager.class)) {
            controllers.add(clazz);
        }
    }

    private void addHandler(final Class<?> controller) {
        Arrays.stream(controller.getDeclaredMethods())
                .forEach(this::addHandlerByMethod);
    }

    private void addHandlerByMethod(final Method method) {
        if (method.isAnnotationPresent(ExceptionHandler.class)) {
            final ExceptionHandler exceptionHandler = method.getAnnotation(ExceptionHandler.class);

            final HandlerExecution handlerExecution = new HandlerExecution(method);
            handlerExecutions.put(exceptionHandler.value(), handlerExecution);
        }
    }

    @Override
    public Object getHandler(final Class<? extends Throwable> exception) {
        return handlerExecutions.get(exception);
    }
}
