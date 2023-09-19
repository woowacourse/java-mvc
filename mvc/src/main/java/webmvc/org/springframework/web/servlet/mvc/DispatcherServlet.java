package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerMappingException;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private final Set<HandlerMapping> handlerMappings = new LinkedHashSet<>();
    private final Set<HandlerAdapter> handlerAdapters = new LinkedHashSet<>();

    public DispatcherServlet() {
        handlerMappings.add(new AnnotationHandlerMapping());
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    // TODO handlerAdapters, handlerMappings 를 별도로 관리하면 서로 전혀 지원하지 않는 Adapter만 있을 수도 있지 않을까?
    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerMapping(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = matchHandler(request);
            adaptHandler(request, response, handler);
        } catch (Exception exception) {
            log.error("Exception : {}", exception.getMessage(), exception);
            throw new ServletException(exception.getMessage());
        }
    }

    private Object matchHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new HandlerMappingException("cannot find handler for request"));
    }

    private void adaptHandler(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .ifPresent(handlerAdapter -> handlerAdapter.handle(request, response, handler));
    }

}
