package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class AnnotationHandler implements Handler {

    private final HandlerExecution handlerExecution;

    public AnnotationHandler(final HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final ModelAndView modelAndView = handlerExecution.handle(request, response);
        final Map<String, Object> model = modelAndView.getModel();
        modelAndView.getView().render(model, request, response);
        return modelAndView;
    }
}
