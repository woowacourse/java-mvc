package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adaptor.HandlerAdapters;
import nextstep.mvc.handler.exception.ExceptionHandlerExecutor;
import nextstep.mvc.handler.tobe.HandlerMapping;
import nextstep.mvc.handler.tobe.HandlerMappings;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.resolver.ViewResolvers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;
    private final ViewResolvers viewResolvers;
    private final ExceptionHandlerExecutor exceptionHandlerExecutor;

    public DispatcherServlet(HandlerMappings handlerMappings, HandlerAdapters handlerAdapters,
                             ViewResolvers viewResolvers, ExceptionHandlerExecutor exceptionHandlerExecutor) {
        this.handlerMappings = handlerMappings;
        this.handlerAdapters = handlerAdapters;
        this.viewResolvers = viewResolvers;
        this.exceptionHandlerExecutor = exceptionHandlerExecutor;
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
            View view = viewResolvers.resolve(modelAndView.getViewName());
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    public ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        try {
            Object handler = handlerMappings.getHandler(request);
            return handlerAdapters.service(request, response, handler);
        } catch (Exception e) {
            return exceptionHandlerExecutor.execute(e);
        }
    }
}
