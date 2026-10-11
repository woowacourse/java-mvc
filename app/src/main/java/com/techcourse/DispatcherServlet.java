package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private static final String BASE_PACKAGE = "com.techcourse";
    private static final String NOT_FOUND_VIEW = "/404.jsp";

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        final var annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        final var manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        handlerMappings = List.of(annotationHandlerMapping, manualHandlerMapping);
        handlerAdapters = List.of(new HandlerExecutionHandlerAdapter(), new ControllerHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Optional<Object> handler = getHandler(request);
            if (handler.isEmpty()) {
                renderNotFound(request, response);
                return;
            }

            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler.get());
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler.get());
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Optional<Object> getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("핸들러를 처리할 수 있는 어댑터가 없습니다: " + handler.getClass().getName()));
    }

    private void renderNotFound(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        new JspView(NOT_FOUND_VIEW).render(Map.of(), request, response);
    }
}
