package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.HandlerExecutionAdapter;
import nextstep.mvc.controller.HandlerExecutionAdapter.Builder;
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
    private HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappings();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    public void init() {
        setDefaultHandlerAdapters();
        handlerMappings.initializeAll();
    }

    private void setDefaultHandlerAdapters() {
        HandlerExecutionAdapter defaultHandlerExecutionAdapter = new Builder().setDefault().build();
        this.handlerAdapters = new HandlerAdapters(defaultHandlerExecutionAdapter);
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
