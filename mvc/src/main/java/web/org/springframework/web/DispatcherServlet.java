package web.org.springframework.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.support.handler.HandlerAdapters;
import web.org.springframework.web.support.handler.HandlerMappers;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapper;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappers handlerMappers = new HandlerMappers();
    private final HandlerAdapters handlerAdapters = new HandlerAdapters();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappers.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        final Object handler = handlerMappers.getHandler(request);
        final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        try {
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(request, response, modelAndView);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(HttpServletRequest req, HttpServletResponse res, ModelAndView mav) throws Exception {
        final View view = mav.getView();
        view.render(mav.getModel(), req, res);
    }

    public void addHandlerMapping(HandlerMapper handlerMapper) {
        handlerMappers.addHandlerMapper(handlerMapper);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.addHandlerAdapter(handlerAdapter);
    }

}
