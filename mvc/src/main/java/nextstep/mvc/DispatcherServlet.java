package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerMappingRegistry;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappingRegistry();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.init();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            doProcess(request, response);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void doProcess(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object handler = getHandler(request);
        final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        modelAndView.render(request, response);
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.getHandler(request)
                .orElseThrow(() -> new NoSuchElementException("필요한 HandlerMapping을 찾을 수 없습니다."));
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("필요한 HandlerAdapter를 찾을 수 없습니다."));
    }
}
