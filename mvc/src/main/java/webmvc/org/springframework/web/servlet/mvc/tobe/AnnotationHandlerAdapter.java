package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupport(Object object) {
        return object instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            Object object,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        HandlerExecution execution = (HandlerExecution) object;

        return execution.handle(request, response);
    }

}
