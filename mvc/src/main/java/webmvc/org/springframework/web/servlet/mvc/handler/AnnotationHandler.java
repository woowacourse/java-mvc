package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class AnnotationHandler implements Handler {

    private final HandlerExecution handlerExecution;

    public AnnotationHandler(final HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            final ModelAndView modelAndView = handlerExecution.handle(request, response);

            final View view = modelAndView.getView();
            final Map<String, Object> model = modelAndView.getModel();

            view.render(model, request, response);
        } catch (final Exception e) {
            throw new ServletException(e.getMessage());
        }
    }
}
