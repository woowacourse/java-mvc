package webmvc.org.springframework.web.servlet;

import webmvc.org.springframework.web.servlet.exception.HandlerAdapterNotFoundException;
import webmvc.org.springframework.web.servlet.exception.HandlerNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationExceptionHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<ExceptionHandlerMapping> exceptionHandlerMappings = List.of(
            new AnnotationExceptionHandlerMapping("com/techcourse")
    );

    private List<HandlerMapping> handlerMappings = List.of(
            new AnnotationHandlerMapping("com/techcourse")
    );

    private List<HandlerAdapter> handlerAdapters = List.of(
            new HandlerExecutionHandlerAdapter()
    );

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        for (ExceptionHandlerMapping exceptionHandlerMapping : exceptionHandlerMappings) {
            exceptionHandlerMapping.initialize();
        }
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = findHandler(request);

            final HandlerAdapter handlerAdapter = findHandlerAdaptor(handler);
            final ModelAndView modelAndView = handlerAdapter.invoke(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            handleException(request, response, e);
        }
    }

    private Object findHandler(final HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (Objects.nonNull(handler)) {
                return handler;
            }
        }
        throw new HandlerNotFoundException("Handler Not found");
    }

    private HandlerAdapter findHandlerAdaptor(final Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }
        throw new HandlerAdapterNotFoundException("Handler Adaptor Not found");
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
            final HandlerAdapter handlerAdapter = findHandlerAdaptor(handler);
            final ModelAndView modelAndView = handlerAdapter.invoke(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception exception) {
            log.error("Exception caused in ExceptionHandler");
            throw new RuntimeException(e);
        }
    }

    private static Object findExceptionHandler(final Throwable e, final ExceptionHandlerMapping exceptionHandlerMapping) {
        return exceptionHandlerMapping.getHandler(e.getClass());
    }
}
