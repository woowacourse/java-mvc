package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.util.PackageUtil;
import nextstep.web.annotation.Controller;
import nextstep.web.support.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        for (Object packageName : basePackage) {
            List<String> classFqcns = PackageUtil.getClassNamesInPackage((String) packageName);
            addHandlerExecutions(classFqcns);
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    private void addHandlerExecutions(final List<String> classFqcns) {
        List<Class<?>> controllerClasses = getControllerClasses(classFqcns);

        for (Class<?> clazz : controllerClasses) {
            for (Method method : clazz.getDeclaredMethods()) {
                List<HandlerKey> handlerKeys = HandlerKey.from(method);
                HandlerExecution handlerExecution = new HandlerExecution(clazz, method);

                for (HandlerKey handlerKey : handlerKeys) {
                    handlerExecutions.put(handlerKey, handlerExecution);
                }
            }
        }
    }

    private List<Class<?>> getControllerClasses(final List<String> classFqcns) {
        return classFqcns.stream()
                .map(this::getClassByFqcn)
                .filter(it -> it.getAnnotation(Controller.class) != null)
                .collect(Collectors.toList());
    }

    private Class<?> getClassByFqcn(final String fqcn) {
        try {
            return Class.forName(fqcn);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("경로에 해당하는 클래스를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }
    }

    public Object getHandler(final HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        HandlerKey handlerKey = new HandlerKey(requestUrl, requestMethod);
        HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

        if (handlerExecution == null) {
            throw new IllegalArgumentException("컨트롤러를 찾을 수 없습니다.");
            // TODO: 적절한 예외로 변경
        }

        return handlerExecution;
    }
}
