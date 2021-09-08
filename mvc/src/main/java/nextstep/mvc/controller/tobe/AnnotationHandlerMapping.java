package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;

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
            Reflections reflections = new Reflections(basePackage);
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
