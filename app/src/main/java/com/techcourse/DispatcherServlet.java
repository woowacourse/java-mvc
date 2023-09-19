package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerMappings;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappings();
    }

    @Override
    public void init() {
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerMappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Optional<Object> handler = handlerMappings.findHandlerMapping(request);
            if (handler.isPresent()) {
                final Object foundHandler = handler.get();
                if (foundHandler instanceof Controller) {
                    final Controller controller = (Controller) handler.get();
                    final String viewName = controller.execute(request, response);
                    move(viewName, request, response);
                    return;
                }
                if (foundHandler instanceof HandlerExecution) {
                    final HandlerExecution handlerExecution = (HandlerExecution) handler.get();
                    final ModelAndView modelAndView = handlerExecution.handle(request, response);
                    final Map<String, Object> model = modelAndView.getModel();
                    final View view = modelAndView.getView();
                    view.render(model, request, response);
                }
            }
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request,
                      final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
