package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;

public class AnnotationHandler implements Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationHandler.class);

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
        } catch (final Throwable e) {
            LOGGER.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
