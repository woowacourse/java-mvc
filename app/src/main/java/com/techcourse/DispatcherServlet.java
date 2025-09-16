package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = List.of(
                new AnnotationHandlerMapping(),
                new ManualHandlerMapping()
        );
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
        handlerAdapters = List.of(
                new AnnotationHandlerAdapter(),
                new ManualHandlerAdapter()
        );
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object foundHandler = null;
            for (HandlerMapping handlerMapping : handlerMappings) {
                final var handler = handlerMapping.getHandler(request);
                if (handler != null) foundHandler = handler;
            }

            if (foundHandler == null) throw new IllegalArgumentException("No HandlerMapping Found For " + requestURI);

            ModelAndView resultModelAndView = null;
            for (HandlerAdapter handlerAdapter : handlerAdapters) {
                ModelAndView modelAndView = handlerAdapter.handle(request, response, foundHandler);
                if (modelAndView != null) resultModelAndView = modelAndView;
            }

            if (resultModelAndView == null) throw new IllegalArgumentException("No HandlerAdapter Found For " + requestURI);

            resultModelAndView.getView().render(resultModelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
