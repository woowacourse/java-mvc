package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class ForwardingExecution extends HandlerExecution {

    private final ModelAndView modelAndView;

    public ForwardingExecution(final ModelAndView modelAndView) {
        super(null, null);
        this.modelAndView = modelAndView;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return modelAndView;
    }
}
