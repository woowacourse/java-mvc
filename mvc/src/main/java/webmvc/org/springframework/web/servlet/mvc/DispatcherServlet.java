package webmvc.org.springframework.web.servlet.mvc;

import context.org.springframework.stereotype.ControllerAdvice;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

import java.util.NoSuchElementException;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private final transient HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final transient HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private final transient ComponentScanner componentScanner;
    private transient HandlerExecutor handlerExecutor;

    public DispatcherServlet(Object... basePackages) {
        componentScanner = new ComponentScanner(basePackages);
        addHandlerMapping(new AnnotationHandlerMapping(componentScanner));
        addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        addExceptionHandlerAdapter();
    }

    @Override
    public void init() {
        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addExceptionHandlerAdapter() {
        final Optional<Class<?>> clazz = componentScanner.getSingleTypeAnnotateWith(ControllerAdvice.class);

        if (clazz.isPresent()) {
            final Object controllerAdvice = componentScanner.createInstance(clazz.get());
            handlerAdapterRegistry.addHandlerAdapter((HandlerAdapter) controllerAdvice);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappingRegistry.getHandler(request);
            if (handler.isEmpty()) {
                throw new NoSuchElementException("Cannot find handler for request");
            }

            handlerExecutor.render(request, response, handler.get());
        } catch (Exception exception) {
            log.error("Exception : {} {}", exception.getMessage(), exception.getStackTrace());

            handlerExecutor.render(request, response, exception);
        }
    }

}
