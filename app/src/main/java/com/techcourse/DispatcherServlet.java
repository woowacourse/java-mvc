package com.techcourse;

import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.apache.el.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.HandlerMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final List<String> LEGACY_NAMES = List.of("/", "/login", "/login/view", "/logout",
            "/register/view");

    private Object[] basePackage;
    private ManualHandlerMapping manualHandlerMapping;
    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        handlerMappings = new ArrayList<>();
        handlerMappings.add(new ManualHandlerMappingAdapter(new ManualHandlerMapping()));
        handlerMappings.add(new AnnotationHandlerMapping(basePackage));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.info("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            if (LEGACY_NAMES.contains(requestURI)) {
                final var controller = manualHandlerMapping.getHandler(requestURI);
                final var viewName = controller.execute(request, response);
                move(viewName, request, response);
                return;
            }
            final HandlerExecution handlerExecution = getHandlerExecution(request);
            final ModelAndView modelAndView = handlerExecution.handle(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerExecution getHandlerExecution(final HttpServletRequest request) throws Exception {
        for (final HandlerMapping handlerMapping : handlerMappings) {
            final HandlerExecution handlerExecution = handlerMapping.getHandler(request);
            if (Objects.nonNull(handlerExecution)) {
                return handlerExecution;
            }
        }
        return null;
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
