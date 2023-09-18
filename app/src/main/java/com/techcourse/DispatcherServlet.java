package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.DefaultHandler;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        try {
            handlerMappings = List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping("com.techcourse"));
            for (HandlerMapping handlerMapping : handlerMappings) {
                handlerMapping.initialize();
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Handler handler = handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(request))
                    .filter(Handler::isSupport)
                    .findFirst()
                    .orElse(new DefaultHandler());
            final ModelAndView modelAndView = handler.handle(request, response);
            final Map<String, Object> model = modelAndView.getModel();
            modelAndView.getView().render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}


