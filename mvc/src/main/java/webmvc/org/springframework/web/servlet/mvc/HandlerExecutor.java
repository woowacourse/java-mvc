package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

public class HandlerExecutor {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutor.class);
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public HandlerExecutor(final HandlerAdapterRegistry handlerAdapterRegistry) {
        this.handlerAdapterRegistry = handlerAdapterRegistry;
    }

    public void execute(final HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            if (handlerAdapter.isPresent()) {
                final var adapter = handlerAdapter.get();
                final var modelAndView = adapter.handle(request, response, handler);

                modelAndView.render(request, response);
                return;
            }

            throw new NoSuchElementException("Cannot find handler adapter for " + handler.getClass().getCanonicalName());
        } catch (InvocationTargetException exception) {
            final var targetException = exception.getTargetException();
            log.error("Unexpected exception: {}", targetException.getMessage());
            executeExceptionHandler(request, response, targetException);
        } catch (Exception exception) {
            log.error("Unexpected exception: {}", exception.getMessage());
            executeExceptionHandler(request, response, exception);
        }
    }

    private void executeExceptionHandler(HttpServletRequest request, HttpServletResponse response, Throwable cause) {
        handlerAdapterRegistry.getHandlerAdapter(cause)
                .ifPresent(exceptionHandlerAdapter -> execute(request, response, cause));
    }

}
