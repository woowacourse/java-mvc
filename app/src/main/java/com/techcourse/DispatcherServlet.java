package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<MappingHandler> mappingHandlerList;

    public DispatcherServlet() {
        this.mappingHandlerList =
                List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping("com.techcourse.controller"));
    }

    private Object findHandler(HttpServletRequest request) {
        for (MappingHandler mappingHandler : mappingHandlerList) {
            Object handler = mappingHandler.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("처리할 수 없는 요청입니다.");
    }

    @Override
    public void init() {
        for (MappingHandler mappingHandler : mappingHandlerList) {
            mappingHandler.initialize();
        }
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        try {
            doHandle(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = findHandler(request);

        if (handler instanceof Controller) {
            final var viewName = ((Controller) handler).execute(request, response);
            move(viewName, request, response);
            return;
        }

        if (handler instanceof AnnotationHandlerMapping) {
            HandlerExecution handlerExecution = ((AnnotationHandlerMapping) handler).getHandler(request);
            ModelAndView modelAndView = handlerExecution.handle(request);
            Map<String, ?> model = modelAndView.getModel();
            modelAndView.getView()
                    .render(model, request, response);
            return;
        }

        throw new IllegalArgumentException("지원하지 않는 핸들러입니다..");
    }

    private void move(
            final String viewName,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
