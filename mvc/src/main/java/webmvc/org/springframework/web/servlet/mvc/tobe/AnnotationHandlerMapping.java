package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

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
            for (Object base : basePackage) {
                File[] javaClasses = new File(Thread.currentThread()
                        .getContextClassLoader()
                        .getResource((String) base)
                        .getPath()).listFiles();

                for (File javaFile : javaClasses) {
                    String fileName = javaFile.getName();

                    if (fileName.contains(".")) {
                        fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    }

                    Class<?> clazz = Class.forName(base + "." + fileName);
                    Method[] declaredMethods = clazz.getDeclaredMethods();

                    for (Method method : declaredMethods) {
                        if (!method.isAnnotationPresent(RequestMapping.class)) {
                            continue;
                        }

                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                        String value = annotation.value();
                        RequestMethod[] requestMethods = annotation.method();

                        for (RequestMethod requestMethod : requestMethods) {
                            HandlerKey handlerKey = new HandlerKey(value, requestMethod);
                            HandlerExecution handlerExecution = new HandlerExecution(clazz.getConstructor().newInstance(), method);
                            handlerExecutions.put(handlerKey, handlerExecution);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey key = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));

        return handlerExecutions.get(key);
    }

}
