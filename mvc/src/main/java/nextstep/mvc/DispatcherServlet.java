package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapterRegistry;
import nextstep.mvc.executor.HandlerExecutor;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.mapping.HandlerMappingRegistry;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;
    private final HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
        this.handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final Optional<Object> handler = handlerMappingRegistry.getHandlerMapping(request);
            if (handler.isEmpty()) {
                log.info("찾을 수 없는 핸들러입니다.");
                response.setStatus(404);
                final View view = new JspView("redirect:/404.jsp");
                view.render(new HashMap<>(), request, response);
                return;
            }
            final ModelAndView modelAndView = handlerExecutor.handle(request, response, handler.get());
            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
