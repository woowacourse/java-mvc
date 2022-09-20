package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        preprocess();
    }

    private void preprocess() {
        Reflections reflections = new Reflections(basePackage);
        List<Method> extractedMethods = reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(each -> Arrays.stream(each.getMethods())
                        .filter(temp -> temp.isAnnotationPresent(RequestMapping.class)))
                .collect(Collectors.toUnmodifiableList());
        List<HandlerKey> handlerKeys = extractedMethods.stream()
                .flatMap(each ->
                        Arrays.stream(each.getAnnotation(RequestMapping.class).method())
                                .map(requestMethod -> new HandlerKey(each.getAnnotation(RequestMapping.class).value(),
                                        requestMethod)))
                .collect(Collectors.toUnmodifiableList());
        List<HandlerExecution> executions = extractedMethods.stream()
                .map(HandlerExecution::new)
                .collect(Collectors.toUnmodifiableList());
        if (handlerKeys.size() != executions.size()) {
            throw new IllegalArgumentException("Handler Key의 갯수와 Handler Execution의 갯수가 동일하지 않습니다.");
        }
        for (int i = 0; i < handlerKeys.size(); i++) {
            this.handlerExecutions.put(handlerKeys.get(i), executions.get(i));
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        if (!this.handlerExecutions.containsKey(handlerKey)) {
            throw new IllegalArgumentException("request에 대응되는 메소드가 없습니다.");
        }
        return handlerExecutions.get(handlerKey);
    }
}
