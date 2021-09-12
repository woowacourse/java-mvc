package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

public class AnnotationHandlerMapping implements HandlerMapping {

    private final Object[] basePackage;
    private final HandlerExecutions handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HandlerExecutions();
    }

    @Override
    public void initialize() {
        try {
            // TODO: MethodAnnotationsScanner 사용 위치 알아보고 기입 결정.
            Reflections reflections = new Reflections(basePackage, new TypeAnnotationsScanner(), new SubTypesScanner());
            Set<Class<?>> annotatedHandlers = reflections.getTypesAnnotatedWith(Controller.class);

            handlerExecutions.initializeWith(annotatedHandlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        return handlerExecutions.getHandlerExecution(uri, requestMethod);
    }
}
