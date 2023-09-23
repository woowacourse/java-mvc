package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerAdapterNotExistException;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotExistException;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.HandlerMappings;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings = new HandlerMappings();
    private HandlerAdapters handlerAdapters = new HandlerAdapters();

    @Override
    public void init() {
        final String packageName = getClass().getPackageName();
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping(packageName));
        handlerAdapters.addAdapter(new ControllerHandlerAdapter());
        handlerAdapters.addAdapter(new AnnotationHandlerAdapter());
        handlerMappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            final Object handler = handlerMappings.getHandler(request);
            final ModelAndView modelAndView = handlerAdapters.handle(handler, request, response);
            final View view = modelAndView.getView();
            view.render(Collections.emptyMap(), request, response);
        } catch (HandlerNotExistException | HandlerAdapterNotExistException exception) {
            setNotFound(request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private void setNotFound(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException, IOException {
        final RequestDispatcher requestDispatcher = request.getRequestDispatcher("404.jsp");
        response.setStatus(404);
        requestDispatcher.forward(request, response);
    }
}
