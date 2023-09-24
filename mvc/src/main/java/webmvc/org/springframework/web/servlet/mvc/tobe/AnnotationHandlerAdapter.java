package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public void execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final HandlerExecution execution = (HandlerExecution) handler;
        final ModelAndView mav = execution.handle(request, response);
        final Map<String, Object> model = mav.getModel();
        final View view = mav.getView();
        view.render(model, request, response);
    }
}
