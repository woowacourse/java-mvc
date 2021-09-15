package nextstep.mvc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.ExecutionHandlerAdapter;
import nextstep.mvc.view.ModelAndView;
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
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                handlerMapping.initialize();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        initHandlerAdapter();
    }

    private void initHandlerAdapter() {
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new ExecutionHandlerAdapter());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        getHandler(request).ifPresentOrElse(obj -> {
            try {
                HandlerAdapter adapter = getHandlerAdapter(obj);
                ModelAndView modelAndView = adapter.handle(request, response, obj);
                modelAndView.render(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }, () -> response.setStatus(HttpServletResponse.SC_NOT_FOUND));
    }

    private Optional<Object> getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst();
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new RuntimeException("HandlerAdapter Not Found");
    }
}
