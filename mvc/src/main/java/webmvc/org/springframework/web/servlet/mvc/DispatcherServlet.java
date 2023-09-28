package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerNotFoundException;
import webmvc.org.springframework.web.servlet.mvc.exception.NotFoundException;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdaptor;

import java.io.IOException;

public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);


    HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    HandlerAdaptorRegistry handlerAdaptorRegistry = new HandlerAdaptorRegistry();

    public DispatcherServlet() {
    }

    public void addHandlerAdaptor(final HandlerAdaptor handlerAdaptor) {
        handlerAdaptorRegistry.addHandlerAdaptor(handlerAdaptor);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    public void init() {
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerAdaptorRegistry.addHandlerAdaptor(new HandlerExecutionHandlerAdaptor());
        handlerMappingRegistry.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.info("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new HandlerNotFoundException("Handler Not Found"));
            final HandlerAdaptor handlerAdaptor = handlerAdaptorRegistry.getHandlerAdaptor(handler);
            final ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (NotFoundException e) {
            log.error("Exception : {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

}
