package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappingList;


    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappingList = List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping());
        handlerMappingList.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException { // TODO refactor
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerMapping handlerMapping = handlerMappingList.stream()
                    .filter(handler -> handler.canHandle(request))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No handler found for request URI: " + requestURI));

            if (handlerMapping instanceof ManualHandlerMapping) {
                final var controller = (Controller) handlerMapping.getHandler(request);
                final var viewName = controller.execute(request, response);
                View view = new JspView(viewName);
                view.render(new HashMap<>(), request, response);
            } else if (handlerMapping instanceof AnnotationHandlerMapping) {
                final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
                ModelAndView modelAndView = handlerExecution.handle(request, response);
                View view = modelAndView.getView();
                view.render(modelAndView.getModel(), request, response);
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
