package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.adapter.HandlerAdapters;
import com.techcourse.ManualHandlerAdapter;
import webmvc.org.springframework.web.servlet.handlermapping.HandlerMappings;
import webmvc.org.springframework.web.servlet.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.handlermapping.AnnotationHandlerMapping;
import com.techcourse.ManualHandlerMapping;
import webmvc.org.springframework.web.servlet.view.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapter();
    }

    private void initHandlerMappings() {
        final HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping());
        this.handlerMappings = handlerMappings;
    }

    private void initHandlerAdapter() {
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
        this.handlerAdapters = handlerAdapters;
    }

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws ServletException {
        final String requestURI = req.getRequestURI();
        log.debug("Method : {}, Request URI : {}", req.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.getHandler(req);
            final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(req, res, handler);
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), req, res);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
