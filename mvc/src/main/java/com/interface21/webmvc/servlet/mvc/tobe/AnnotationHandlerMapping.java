package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.core.util.ReflectionUtils;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 여기서 HandlerKey, HandlerExecution을 넣어줘야 한다.

        // TODO: 아직 base가 어떤 기준으로 선정되는지 모름.
        for (Object base : basePackage) {
            String packagePath = String.valueOf(base);
            log.info("basePackage: {}", packagePath);

            // 패키지에 있는 애들을 가져와야 하므로, Reflections 라이브러리를 활용한다.
            Reflections reflections = new Reflections(packagePath);
            // 1. @Controller가 붙은 클래스를 찾는다.
            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
            log.info("@Controller.class가 붙은 클래스 조회 성공");

            for (Class<?> aClass : classes) {
                log.info("@RequestMapping이 붙은 메서드 조회 시작");
                Method[] methods = aClass.getMethods();

                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        log.info("@RequestMapping이 붙은 메서드 조회 성공, methodName = {}", method.getName());

                        log.info("HandlerKey 생성 시작");
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String url = requestMapping.value();
                        // TODO: 여러 메서드도 지원할 수 있게 변경해야 한다. 지금은 하나만 있다고 가정한다. -> RequestMethod 루프 돌면서 처리하면 될듯
                        RequestMethod requestMethod = requestMapping.method()[0];
                        HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                        log.info("HandlerKey 생성 완료, url = {}, requestMethod = {}", url, requestMethod);

                        log.info("HandlerExecution 생성 시작");
                        Constructor<?> constructor = ReflectionUtils.accessibleConstructor(aClass);
                        Object controller = constructor.newInstance();
                        HandlerExecution handlerExecution = new HandlerExecution(controller, method);
                        log.info("HandlerExecution 생성 완료, className = {}, methodName = {}", aClass.getName(),
                                method.getName());

                        log.info("handlerExecutions에 저장 시작");
                        handlerExecutions.put(handlerKey, handlerExecution);
                    }
                }

            }
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        log.debug("getHandler 호출");
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Map에 존재하는 HandlerKey를 조회해야 한다.
        return handlerExecutions.get(new HandlerKey(requestURI, RequestMethod.valueOf(method)));
    }
}
