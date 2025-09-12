package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
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
    // HandlerKey: 요청 매핑을 위해 사용되는 키
    // HandlerExecution: 매핑된 컨트롤러 메서드를 실제 실행하는 역할을 가지는 객체
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    // 1. @Controller 붙어있는 클래스를 찾아서
    public void initialize() {
        for (final Object basePackage : basePackage) {
            final Reflections reflections = new Reflections(basePackage);
            final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
            
            initializeControllers(controllers);
        }
        log.info("Initialized AnnotationHandlerMapping!");
    }

    // 2. 각 Controller마다 객체 생성 및 메서드 목록을 가져와서
    private void initializeControllers(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            try {
                initializeMethods(controller.getDeclaredConstructor().newInstance(), controller.getMethods()); // Controller 세팅
            } catch (Exception e) {
                log.error("Handler의 Method를 매핑하는 과정에서 오류가 발생했습니다.", e);
            }
        }
    }

    // 3. 각 메서드마다
    private void initializeMethods(Object controller, Method[] methods) {
        for (Method method : methods) {
            initializeMethod(controller, method);
        }
    }

    // 4. @RequestMapping을 확인한 후
    private void initializeMethod(Object controller, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping == null) {
            return;
        }

        String url = requestMapping.value(); // 5. 해당 요청 경로와
        RequestMethod[] requestMethods = getRequestMethods(requestMapping);
        initializeHandlerExecution(controller, method, requestMethods, url);
    }

    // 6. http 메서드 목록을 가져온 다음에
    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        RequestMethod[] requestMethods = requestMapping.method();
        if (requestMethods.length == 0) {
            requestMethods = RequestMethod.values();
        }

        return requestMethods;
    }

    private void initializeHandlerExecution(Object controller, Method method, RequestMethod[] requestMethods, String url) {
        for (RequestMethod requestMethod : requestMethods) {
            HandlerKey handlerKey = new HandlerKey(url, requestMethod); // 7. url과 http method로 key를 만들어서
            HandlerExecution handlerExecution = new HandlerExecution(controller, method); // 8. 매핑된 객체를 실행하는 핸들러 세팅 후
            handlerExecutions.put(handlerKey, handlerExecution); // 9. map 목록에 추가!
        }
    }

    // 실제로 요청이 들어온다면
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI(); // URI를 가져오고
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod()); // Http Method를 확인한 다음에
        HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod); // 두 정보를 가지고 key를 만들어서

        return handlerExecutions.get(handlerKey); // 핸들러 객체를 가져오기!!
    }
}
