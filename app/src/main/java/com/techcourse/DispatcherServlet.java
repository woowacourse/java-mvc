package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String ERROR_404_PAGE = "/404.jsp";
    private static final String ERROR_500_PAGE = "/500.jsp";

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();
    }

    @Override
    public void init() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);

        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new IllegalArgumentException("요청 URI에 대한 핸들러가 등록되지 않았습니다: " + requestURI));

            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler)
                    .orElseThrow(() -> new IllegalStateException(
                            "핸들러 타입에 대한 어댑터를 찾을 수 없습니다: " + handler.getClass().getName()));

            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

            render(modelAndView, request, response);
        } catch (Exception e) {
            handleError(e, request, response);
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            if (modelAndView == null) {
                throw new IllegalStateException("HandlerAdapter가 ModelAndView를 반환하지 않았습니다");
            }

            if (modelAndView.getView() == null) {
                throw new IllegalStateException("ModelAndView에 View가 설정되지 않았습니다");
            }

            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handleError(Exception ex, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(ex instanceof IllegalArgumentException) {
            log.error(ex.getMessage());
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            renderErrorPage(ERROR_404_PAGE, request, response);
            return;
        }

        if (ex instanceof IllegalStateException) {
            log.error(ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            renderErrorPage(ERROR_500_PAGE, request, response);
            return;
        }

        log.error("Exception : {}", ex.getMessage(), ex);
        throw new ServletException(ex.getMessage());
    }

    private void renderErrorPage(String errorPage, HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        final View errorView = new JspView(errorPage);
        final ModelAndView modelAndView = new ModelAndView(errorView);
        render(modelAndView, request, response);
    }
}
