package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.WebApplicationInitializer;
import jakarta.servlet.ServletContext;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for {@link WebApplicationInitializer} implementations that register a {@link DispatcherServlet} in the
 * servlet context.
 */
public class DispatcherServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServletInitializer.class);

    private static final String DEFAULT_SERVLET_NAME = "dispatcher";

    @Override
    public void onStartup(final ServletContext servletContext) {
        String mainClassPackageName = getMainClassPackageName();

        final var dispatcherServlet = new DispatcherServlet(mainClassPackageName);

        final var registration = servletContext.addServlet(DEFAULT_SERVLET_NAME, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("Failed to register servlet with name '" + DEFAULT_SERVLET_NAME + "'. " +
                    "Check if there is another servlet registered under the same name.");
        }

        registration.setLoadOnStartup(1);
        registration.addMapping("/");

        log.info("Start AppWebApplication Initializer");
    }

    private String getMainClassPackageName() {
        List<StackTraceElement> allStackTraceElements = getAllStackTraceElements();
        return allStackTraceElements.stream()
                .filter(stackTraceElement -> stackTraceElement.getMethodName().equals("main")) // "main" 메서드가 있는지 필터링
                .map(StackTraceElement::getClassName) // 클래스 이름으로 매핑
                .map(this::getClass) // 클래스 객체로 변환
                .filter(this::isMainClass) // main 클래스인지 확인
                .map(Class::getPackageName) // 패키지 이름으로 변환
                .findFirst() // 첫 번째 일치하는 값 찾기
                .orElseThrow(() -> new RuntimeException("main 메서드가 있는 클래스를 찾을 수 없습니다.")); // 값이 없을 때 예외 발생
    }

    private List<StackTraceElement> getAllStackTraceElements() {
        return Thread.getAllStackTraces().keySet().stream()
                .flatMap(thread -> Arrays.stream(thread.getStackTrace()))
                .toList();
    }

    private Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isMainClass(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredMethods())
                .anyMatch(this::isMainMethod);
    }

    private boolean isMainMethod(Method method) {
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        boolean isMain = method.getName().equals("main");
        boolean isVoid = method.getReturnType().getName().equals("void");
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> parameterType = parameterTypes[0];
        boolean equals = parameterType.equals(String[].class);
        return isStatic && isMain && isVoid && equals;
    }
}
