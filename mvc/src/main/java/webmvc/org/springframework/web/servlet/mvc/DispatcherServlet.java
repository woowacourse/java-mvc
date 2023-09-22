package webmvc.org.springframework.web.servlet.mvc;

import webmvc.org.springframework.web.servlet.mvc.support.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.support.HandlerExceptionResolvers;
import webmvc.org.springframework.web.servlet.mvc.support.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappings handlerMappings = new HandlerMappings();
    private final transient HandlerAdapters handlerAdapters = new HandlerAdapters();
    private final transient HandlerExceptionResolvers handlerExceptionResolvers = new HandlerExceptionResolvers();

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.addHandlerAdapter(handlerAdapter);
    }

    public void addHandlerExceptionResolvers(HandlerExceptionResolver handlerExceptionResolver) {
        handlerExceptionResolvers.addHandlerExceptionResolver(handlerExceptionResolver);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        log.debug("Method : {}, Request URI : {}", req.getMethod(), req.getRequestURI());
        try {
            Object handler = handlerMappings.getHandler(req);
            HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView mav = adapter.handle(req, res, handler);
            render(mav, req, res);
        } catch (Exception e) {
            resolveException(req, res, e);
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse res) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, res);
    }

    private void resolveException(HttpServletRequest req, HttpServletResponse res, Exception ex) throws ServletException {
        try {
            HandlerExceptionResolver handlerExceptionResolver = handlerExceptionResolvers.getExceptionResolver(ex);
            ModelAndView mav = handlerExceptionResolver.resolveException(req, res, ex);
            render(mav, req, res);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
