package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecutionAdapter;
import nextstep.mvc.controller.tobe.ParameterResolverExecutor;
import nextstep.mvc.handler.HandlerAdapter;
import nextstep.mvc.handler.HandlerAdapters;
import nextstep.mvc.handler.HandlerMapping;
import nextstep.mvc.handler.HandlerMappings;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;
    private final HandlerAdapters handlerAdapters;

    {
        ParameterResolverExecutor defaultParameterResolverExecutor = new ParameterResolverExecutor(
            Collections.emptyList());
        this.handlerAdapters = new HandlerAdapters(List.of(
            new ControllerHandlerAdapter(),
            new HandlerExecutionAdapter(defaultParameterResolverExecutor)
        ));
    }

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappings();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.getHandler(request);
            HandlerAdapter handlerAdapter = handlerAdapters.chooseProperAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
