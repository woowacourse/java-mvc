package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry(new ArrayList<>());
        this.handlerAdapterRegistry = new HandlerAdapterRegistry(new ArrayList<>());
    }

    @Override
    public void init() {
        this.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        this.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);
        if (handler.isEmpty()) {
            throw new IllegalArgumentException("request에 대응되는 handler가 없습니다.");
        }
        Object handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler.get());
        ModelAndView modelAndView = ((HandlerAdapter) handlerAdapter).handle(request, response, handler.get());
        render(modelAndView, request, response);
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
