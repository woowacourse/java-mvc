package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        getHandlerExecution(request).ifPresentOrElse(handler -> {
            try {
                HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
                ModelAndView mav = handlerAdapter.handle(request, response, handler);
                View view = viewResolver(mav);
                view.render(mav.getModel(), request, response);
            } catch (Exception e) {
                log.error("Exception : {}", e.getMessage(), e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }, () -> response.setStatus(HttpServletResponse.SC_NOT_FOUND));
    }

    private Optional<HandlerExecution> getHandlerExecution(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .map(HandlerExecution.class::cast)
                .findAny();
    }

    private HandlerAdapter getHandlerAdapter(HandlerExecution handlerExecution) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handlerExecution))
                .findAny()
                .orElseThrow(() -> new RuntimeException("Adapter Not Found"));
    }

    private View viewResolver(ModelAndView mav) {
        return mav.getView();
    }
}
