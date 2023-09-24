package webmvc.org.springframework.web.servlet.mvc.asis.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.http.exception.MethodNotAllowedException;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handler.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMappings;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;

    public DispatcherServlet() {
        handlerMappings = new HandlerMappings();
    }

    @Override
    public void init() {
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
        handlerMappings.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappings.mapHandler(request);
            final HandlerAdapter handlerAdapter = mapHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.execute(request, response);
            modelAndView.render(request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter mapHandlerAdapter(final Object handler) {
        if (handler instanceof HandlerExecution) {
            return new HandlerExecutionHandlerAdapter(handler);
        }
        throw new MethodNotAllowedException();
    }
}
