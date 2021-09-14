package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapterRegistry;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.executor.HandlerExecutor;
import nextstep.mvc.exeption.HandlerMappingException;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    public void init() {
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final Object handler = getHandler(request);
            final ModelAndView modelAndView = handlerExecutor.handle(request, response, handler);
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerExecution getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (handler instanceof HandlerExecution) {
                return (HandlerExecution) handler;
            }
            return getManualControllerHandler(handlerMapping, request);
        }
        log.info("찾을 수 없는 핸들러 입니다.");
        throw new HandlerMappingException("찾을 수 없는 핸들러 입니다.");
    }

    private HandlerExecution getManualControllerHandler(HandlerMapping handlerMapping, HttpServletRequest request) {
        try {
            final Controller controller = (Controller) findManualController(handlerMapping, request);
            final Method execute = controller.getClass().getMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
            return new HandlerExecution(controller, execute);
        } catch (NoSuchMethodException e) {
            log.info("해당 HTTP 메서드의 컨트롤러가 존재하지 않습니다. 이유: {}", e.getMessage());
            throw new HandlerMappingException("해당 HTTP 메서드의 컨트롤러가 존재하지 않습니다. 이유: " + e.getMessage());
        }
    }

    private Object findManualController(HandlerMapping handlerMapping, HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(it -> !it.equals(handlerMapping))
                .map(it -> it.getHandler(request))
                .filter(Objects::nonNull)
                .map(Controller.class::cast)
                .findFirst()
                .orElseThrow();
    }


}
