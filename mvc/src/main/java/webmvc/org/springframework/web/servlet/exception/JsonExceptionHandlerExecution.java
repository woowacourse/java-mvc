package webmvc.org.springframework.web.servlet.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JsonView;

public class JsonExceptionHandlerExecution implements HandlerExecution {

    private final String message;

    public JsonExceptionHandlerExecution(final String message) {
        this.message = message;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final ExceptionMessage exceptionMessage = new ExceptionMessage(message);
        modelAndView.addObject("error", exceptionMessage);
        return modelAndView;
    }
}
