package com.techcourse;

import java.util.HashMap;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
//            [ Legacy MVC ]
//            final var controller = manualHandlerMapping.getHandler(requestURI);
//            final var viewName = controller.execute(request, response);
//            move(viewName, request, response);

//            [ @MVC ]
//            final HandlerExecution handlerExecution = (HandlerExecution) annotationHandlerMapping.getHandler(request);
//            final var viewNameV2 = handlerExecution.handle(request, response);
//            move(viewNameV2.toString(), request, response);

//            [ 통합 ]
//            final var handler = handlerMapping.getHandler(request);
//            final var view = handlerAdapter.handle(request, response, handler);
//            move(view, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final View view = new JspView(viewName);

        view.render(new HashMap<>(), request, response);
    }
}
