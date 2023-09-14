package com.techcourse;

import com.techcourse.adapter.ManualHandlerMappingAdapter;
import com.techcourse.mapping.ManualHandlerMappingWrapper;
import com.techcourse.view.resolver.JspViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.view.resolver.ViewResolver;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient List<HandlerMapping> mappings = new ArrayList<>();
    private final transient List<HandlerAdapter> adapters = new ArrayList<>();
    private final transient List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init() {
        initHandlerMappings();
        initViewResolvers();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        final ManualHandlerMappingWrapper manualHandlerMapping = new ManualHandlerMappingWrapper();
        manualHandlerMapping.initialize();

        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping();
        annotationHandlerMapping.initialize();

        mappings.add(manualHandlerMapping);
        mappings.add(annotationHandlerMapping);
    }

    private void initViewResolvers() {
        final JspViewResolver jspViewResolver = new JspViewResolver();

        viewResolvers.add(jspViewResolver);
    }

    private void initHandlerAdapters() {
        final ManualHandlerMappingAdapter manualHandlerMappingAdapter = new ManualHandlerMappingAdapter(viewResolvers);

        adapters.add(manualHandlerMappingAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);

            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return ;
            }

            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.execute(request, response, handler);

            move(modelAndView, request, response);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);

            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        for (final HandlerMapping mapping : mappings) {
            final Object handler = mapping.getHandler(request);

            if (handler != null) {
                return handler;
            }
        }

        return null;
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        for (final HandlerAdapter adapter : adapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new IllegalStateException("해당 Handler를 수행할 수 있는 HandlerAdapter가 존재하지 않습니다.");
    }

    /**
     * Legacy MVC + @MVC 통합 과정에서 Controller.execute()로 바로 viewName을 반환받던 코드에서 ModelAndView를 반환받도록 변경이 되었습니다.
     * 그래서 View 관련 코드를 작성하다가 미션을 보니까 3단계...에서 하는 것 같더라고요...
     * 그래서 일단 이렇게 방치를 하게 되었습니다...
     */
    private void move(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        // ignored
    }
}
