package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
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
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;

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
            final List<Class<?>> controllers = new ArrayList<>();
            for (Object path : basePackage) {
                컨트롤러찾기((String) path, controllers);
            }
            for (Class<?> controller : controllers) {
                핸들러추가하기(controller);
            }
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void 컨트롤러찾기(final String 경로, final List<Class<?>> controllers) throws URISyntaxException, IOException {
        final File[] 파일들 = 주어진_경로에_있는_파일들_가져오기(경로);

        for (File 파일 : 파일들) {
            final String 파일경로 = 경로 + "." + 파일.getName();
            if (파일.isDirectory()) {
                컨트롤러찾기(파일경로, controllers);
            }
            if (파일.getName().endsWith(".class")) {
                컨트롤러인지_판별하기(파일경로, controllers);
            }
        }
    }

    private File[] 주어진_경로에_있는_파일들_가져오기(final String 경로) throws URISyntaxException {
        final String packagePath = 경로.replace('.', '/');
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final File 패키지 = new File(classLoader.getResource(packagePath).toURI());
        return 패키지.listFiles();
    }

    private void 컨트롤러인지_판별하기(final String 파일, final List<Class<?>> controllers) {
        try {
            final Class<?> 클래스 = 클래스로_바꾸기(파일);
            if (클래스.isAnnotationPresent(context.org.springframework.stereotype.Controller.class)) {
                controllers.add(클래스);
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private Class<?> 클래스로_바꾸기(final String 파일) throws ClassNotFoundException {
        String className = 파일.replace(".class", "");
        return Class.forName(className);
    }

    private void 핸들러추가하기(final Class<?> controller) {
        for (Method 메서드 : controller.getDeclaredMethods()) {
            if (메서드.isAnnotationPresent(RequestMapping.class)) {
                final RequestMapping requestMapping = 메서드.getAnnotation(RequestMapping.class);
                for (RequestMethod requestMethod : requestMapping.method()) {
                    메서드별로_핸들러추가하기(controller, 메서드, requestMapping, requestMethod);
                }
            }
        }
    }

    private void 메서드별로_핸들러추가하기(
            final Class<?> controller,
            final Method 메서드,
            final RequestMapping requestMapping,
            final RequestMethod requestMethod) {
        try {
            final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);
            final HandlerExecution handlerExecution = new HandlerExecution(
                    controller.getDeclaredConstructor().newInstance(), 메서드);
            handlerExecutions.put(handlerKey, handlerExecution);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        return handlerExecutions.get(
                new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod())));
    }
}
