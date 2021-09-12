package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.adapter.HandlerAdapterRegistry;
import nextstep.mvc.exception.NotFoundException;
import nextstep.mvc.mapping.HandlerMappingRegistry;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public DispatcherServlet() {
        this.handlerMappingRegistry = new HandlerMappingRegistry();
        this.handlerAdapterRegistry = new HandlerAdapterRegistry();
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
    protected void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappingRegistry.getHandlerMapping(request)
                .orElseThrow(NotFoundException::new);

            HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler)
                .orElseThrow();

            ModelAndView mv = handlerAdapter.handle(request, response, handler);

            resolveView(mv, request, response);
        } catch (NotFoundException e) {
            log.error("Exception : {}", e.getMessage());
            render("404.jsp", request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            render("500.jsp", request, response);
        }
    }

    private void resolveView(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }

    private void render(String viewName, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
