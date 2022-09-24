package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = HandlerMappingRegistry.empty();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var handler = getHandler(request);
            final var modelAndView = executeHandler(handler, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new RuntimeException("요청을 처리할 수 없습니다."));
    }

    private ModelAndView executeHandler(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (HandlerExecution.class.isAssignableFrom(handler.getClass())) {
            return ((HandlerExecution) handler).handle(request, response);
        }
        if (Controller.class.isAssignableFrom(handler.getClass())) {
            String viewName = ((Controller) handler).execute(request, response);
            JspView jspView = new JspView(viewName);
            return new ModelAndView(jspView);
        }

        throw new IllegalStateException("지원하는 handler가 존재하지 않습니다.");
    }
}
