package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapterFinder;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.ExceptionResolver;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.HandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMapping handlerMapping;
    private final HandlerAdapterFinder handlerAdapterFinder;
    private final ExceptionResolver exceptionResolver;

    public DispatcherServlet(HandlerMapping handlerMapping, HandlerAdapterFinder handlerAdapterFinder,
                             ExceptionResolver exceptionResolver) {
        this.handlerMapping = handlerMapping;
        this.handlerAdapterFinder = handlerAdapterFinder;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Object handler = handlerMapping.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapterFinder.find(handler);
            ModelAndView mv = handlerAdapter.handle(request, response, handler);
            render(request, response, mv);
        } catch (Throwable e) {
            handleException(request, response, e);
        }
    }

    private void render(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView)
        throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, Throwable e)
        throws ServletException {
        ModelAndView mv = exceptionResolver.handle(e);
        try {
            render(request, response, mv);
        } catch (Exception ex) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
