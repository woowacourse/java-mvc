package nextstep.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return hasAnyRequestMappingAnnotation(handler.getClass());
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }

    private boolean hasAnyRequestMappingAnnotation(Class<?> aClass) {
        boolean typeAnnotated = Arrays.stream(aClass.getAnnotations())
                .anyMatch(it -> it instanceof RequestMapping);

        boolean methodAnnotated = Arrays.stream(aClass.getDeclaredMethods())
                .flatMap(it -> Arrays.stream(it.getAnnotations()))
                .anyMatch(it -> it instanceof RequestMapping);

        return typeAnnotated || methodAnnotated;
    }
}
