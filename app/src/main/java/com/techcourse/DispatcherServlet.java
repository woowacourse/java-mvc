package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
        handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(request))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("요청에 맞는 핸들러가 없습니다."));
            if (handler instanceof Controller controller) {
                final var viewName = controller.execute(request, response);
                move(viewName, request, response);
                return;
            }
            if (handler instanceof HandlerExecution handlerExecution) {
                final var modelAndView = handlerExecution.handle(request, response);
                modelAndView.getView().render(modelAndView.getModel(), request, response);
                return;
            }
            throw new IllegalArgumentException("지원하지 않는 핸들러입니다: " + handler.getClass().getName());
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
