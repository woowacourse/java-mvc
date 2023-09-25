package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webmvc.org.springframework.web.servlet.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdopters handlerAdopters;

    @Override
    public void init() {
        this.handlerMappings = new HandlerMappings(List.of(
                new AnnotationHandlerMapping("com.techcourse.controller")));
        this.handlerAdopters = new HandlerAdopters(List.of(
                new HandlerExecutionAdopter()
        ));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.getHandler(request);
            final HandlerAdopter adopter = handlerAdopters.getAdopter(handler);
            final ModelAndView modelAndView = adopter.handle(handler, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
