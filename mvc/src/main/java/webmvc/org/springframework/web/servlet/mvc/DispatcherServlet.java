package webmvc.org.springframework.web.servlet.mvc;

import core.org.springframework.util.ReflectionUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String INTERNAL_BASE_PACKAGE = "webmvc.org.springframework.web.servlet.mvc";

    private final Object[] externalBasePackages;
    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(Object... externalBasePackages) {
        this.externalBasePackages = externalBasePackages;
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        final var reflections = new Reflections(externalBasePackages, INTERNAL_BASE_PACKAGE);
        initHandlerMappings(reflections);
        initHandlerAdapters(reflections);
    }

    private void initHandlerMappings(final Reflections reflections) {
        final var handlerMappingSubTypes = reflections.getSubTypesOf(HandlerMapping.class);
        addHandlerMappings(handlerMappingSubTypes);
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private void addHandlerMappings(final Set<Class<? extends HandlerMapping>> handlerMappingSubTypes) {
        for (final var handlerMappingSubType : handlerMappingSubTypes) {
            if (handlerMappingSubType.isInterface()) {
                continue;
            }
            // exclude pre-defined handler mapping
            if (handlerMappingSubType.equals(AnnotationHandlerMapping.class)) {
                handlerMappings.add(new AnnotationHandlerMapping(externalBasePackages));
                continue;
            }
            handlerMappings.add(ReflectionUtils.instantiate(handlerMappingSubType));
        }
    }

    private void initHandlerAdapters(final Reflections reflections) {
        final var handlerAdapterSubTypes = reflections.getSubTypesOf(HandlerAdapter.class);
        addHandlerAdapters(handlerAdapterSubTypes);
    }

    private void addHandlerAdapters(final Set<Class<? extends HandlerAdapter>> handlerAdapterSubTypes) {
        for (final var reflectionsSubTypeOf : handlerAdapterSubTypes) {
            if (reflectionsSubTypeOf.isInterface()) {
                continue;
            }
            handlerAdapters.add(ReflectionUtils.instantiate(reflectionsSubTypeOf));
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        final var handler = getHandler(request);
        final var handlerAdapter = getHandlerAdapter(handler);
        try {
            final var modelAndView = handlerAdapter.handle(request, response, handler);
            move(modelAndView.getViewName(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                              .map(mapping -> mapping.getHandler(request))
                              .filter(Objects::nonNull)
                              .findAny()
                              .orElseThrow(() -> new HandlerNotFoundException("Not found handler for request URI : " + request.getRequestURI()));
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                              .filter(adapter -> adapter.supports(handler))
                              .findAny()
                              .orElseThrow(() -> new HandlerNotFoundException("Not found handler adapter for handler : " + handler));
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
