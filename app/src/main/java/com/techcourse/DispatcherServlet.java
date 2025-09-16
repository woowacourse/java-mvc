package com.techcourse;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
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

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = List.of(
                new AnnotationHandlerMapping("com.techcourse"),
                new ManualHandlerMapping()
        );
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            for (HandlerMapping handlerMapping : handlerMappings) {
                final var handler = handlerMapping.getHandler(request);
                switch (handler) {
                    case null -> {
                        continue;
                    }
                    case HandlerExecution handlerExecution -> {
                        final var modelAndView = handlerExecution.handle(request, response);
                        modelAndView.getView().render(modelAndView.getModel(), request, response);
                    }
                    case Controller controller -> {
                        String viewName = controller.execute(request, response);
                        move(viewName, request, response);
                    }
                    default -> throw new IllegalStateException("HandlerType Not Supported, type=" + handler.getClass().getSimpleName());
                }
                return;
            }
            throw new IllegalArgumentException("No Handler Found For " + requestURI);
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
