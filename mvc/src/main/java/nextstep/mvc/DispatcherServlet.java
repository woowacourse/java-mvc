package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adaptor.HandlerAdapters;
import nextstep.mvc.exception.NotFoundException;
import nextstep.mvc.exception.handler.AnnotationExceptionHandlerMapping;
import nextstep.mvc.exception.handler.ExceptionHandlerExecution;
import nextstep.mvc.handler.tobe.HandlerMapping;
import nextstep.mvc.handler.tobe.HandlerMappings;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.resolver.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    private final ViewResolver viewResolver;
    private final AnnotationExceptionHandlerMapping exceptionHandlerMapping;

    public DispatcherServlet(HandlerMappings handlerMappings, HandlerAdapters handlerAdapters,
            ViewResolver viewResolver) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
        this.viewResolver = viewResolver;
        this.exceptionHandlerMapping = new AnnotationExceptionHandlerMapping();
    }

    @Override
    public void init() {
        handlerMappings.init();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            ModelAndView modelAndView = processRequest(request, response);
            View view = viewResolver.resolve(modelAndView.getViewName());
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Object handler = handlerMappings.getHandler(request);
            return handlerAdapters.service(request, response, handler);
        } catch (NotFoundException notFoundException) {
            Object handler = exceptionHandlerMapping.getHandler(notFoundException.getClass());
            ExceptionHandlerExecution handlerExecution = (ExceptionHandlerExecution)handler;
            return handlerExecution.handle(notFoundException);
        }
    }
}
