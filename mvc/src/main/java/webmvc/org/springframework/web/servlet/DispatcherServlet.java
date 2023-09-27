package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.exception.HandlerAdapterNotFoundException;
import webmvc.org.springframework.web.servlet.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationExceptionHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<ExceptionHandlerMapping> exceptionHandlerMappings = List.of(
            new AnnotationExceptionHandlerMapping("com/techcourse")
    );

    private final HandlerAdapterRegistry handlerAdapterRegistry;

    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet(final HandlerAdapterRegistry handlerAdapterRegistry,
                             final HandlerMappingRegistry handlerMappingRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;
        this.handlerMappingRegistry = handlerMappingRegistry;
    }

    @Override
    public void init() {
        for (ExceptionHandlerMapping exceptionHandlerMapping : exceptionHandlerMappings) {
            exceptionHandlerMapping.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new HandlerNotFoundException("핸들러못찾았썹"));
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler)
                    .orElseThrow(() -> new HandlerAdapterNotFoundException("Handler Adaptor Not found"));

            final ModelAndView modelAndView = handlerAdapter.invoke(handler, request, response);
            modelAndView.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            handleException(request, response, e);
        }
    }

    private void handleException(final HttpServletRequest request, final HttpServletResponse response, final Throwable e) {
        try {Object handler = null;
            for (ExceptionHandlerMapping exceptionHandlerMapping : exceptionHandlerMappings) {
                final Object foundHandler = findExceptionHandler(e, exceptionHandlerMapping);
                if (Objects.nonNull(foundHandler)) {
                    handler = foundHandler;
                    break;
                }
            }

            if (Objects.isNull(handler)) {
                throw new ServletException("ExceptionHandler Not Found.");
            }
            final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler)
                    .orElseThrow(() -> new HandlerAdapterNotFoundException("Handler Adaptor Not found"));

            final ModelAndView modelAndView = handlerAdapter.invoke(handler, request, response);
            modelAndView.render(modelAndView.getModel(), request, response);
        } catch (Exception exception) {
            log.error("Exception caused in ExceptionHandler");
            throw new RuntimeException(e);
        }
    }

    private static Object findExceptionHandler(final Throwable e, final ExceptionHandlerMapping exceptionHandlerMapping) {
        return exceptionHandlerMapping.getHandler(e.getClass());
    }
}
