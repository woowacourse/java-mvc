package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class SimpleForwardingHandlerMapping implements HandlerMapping {

    private final ForwardingExecution forwardingExecution;

    public SimpleForwardingHandlerMapping(final ModelAndView modelAndView) {
        this.forwardingExecution = new ForwardingExecution(modelAndView);
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean isSupport(HttpServletRequest request) {
        return true;
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        return forwardingExecution;
    }
}
