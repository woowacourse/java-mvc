package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private final transient HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final transient HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
    private transient ExceptionHandlerAdapter exceptionHandlerAdapter;

    public DispatcherServlet() {
        addHandlerMapping(new AnnotationHandlerMapping());
        addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    @Override
    public void init() {
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    public void addExceptionHandlerAdapter(ExceptionHandlerAdapter exceptionHandlerAdapter) {
        this.exceptionHandlerAdapter = exceptionHandlerAdapter;
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappingRegistry.matchHandler(request);

            handlerAdapterRegistry.adaptHandler(request, response, handler);
        } catch (Exception exception) {
            log.error("Exception : {}", exception.getMessage(), exception);
            if (exceptionHandlerAdapter != null) {
                exceptionHandlerAdapter.handle(request, response, exception);
                return;
            }

            throw new ServletException(exception.getMessage());
        }
    }

}
