package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.exception.HandlerExecutionException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    /**
     * 이후에 추가적인 기능이 들어갈 가능성이 있다고 생각해서 for 문을 이용했습니다.
     * - stream 을 이용해서 한 번에 작성하게 되면 이후에 추가적인 기능이 필요할 때 리팩터링하기 힘들다고 생각했습니다.
     * - 3중 for 문을 메서드로 분리하지 않고 한 눈에 전체 로직을 볼 수 있도록 했습니다.
     *
     * {@link RequestMapping.java}
     * RequestMapping 의 필드로 RequestMethod 가 배열로 선언이 되어 있어 있는 만큼 Map<HandlerKey, HandlerExecution>에 반복 주입하였습니다.
     * 이후에도 RequestMethod 가 RequestMapping 에 배열로 존재하는 것이 필요 없다고 판단되면 수정하려고 합니다.
     */
    public void initialize() {
        log.info("====================> Initialized AnnotationHandlerMapping!");
        final Set<Class<?>> controllerClazzes = new Reflections(basePackage).getTypesAnnotatedWith(Controller.class);

        for (final Class<?> controllerClazz : controllerClazzes) {
            final List<Method> foundMethods = collectMethodsWithRequestMappingAnnotation(controllerClazz);

            for (final Method method : foundMethods) {
                final RequestMapping requestMappingAnnotation = method.getDeclaredAnnotation(RequestMapping.class);

                for (final RequestMethod requestMethod : requestMappingAnnotation.method()) {
                    final HandlerKey handlerKey = new HandlerKey(requestMappingAnnotation.value(), requestMethod);
                    final HandlerExecution handlerExecution = new HandlerExecution(method);

                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    private List<Method> collectMethodsWithRequestMappingAnnotation(final Class<?> controllerClazz) {
        return Arrays.stream(controllerClazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .collect(Collectors.toList());
    }

    public Object getHandler(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.from(request.getMethod());
        final HandlerKey handlerKey = new HandlerKey(requestUri, requestMethod);

        if (!handlerExecutions.containsKey(handlerKey)) {
            throw new HandlerExecutionException("[ERROR] 입력받은 HandlerKey 로 HandlerExecution 을(를) 찾을 수 없습니다.");
        }

        return handlerExecutions.get(handlerKey);
    }
}
