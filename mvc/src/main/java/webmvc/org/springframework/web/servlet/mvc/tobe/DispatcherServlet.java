package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings;
    private HandlerExecutor handlerExecutor;

    public DispatcherServlet() {
    }

    public DispatcherServlet(HandlerMappings handlerMappings, HandlerExecutor handlerExecutor) {
        this.handlerMappings = handlerMappings;
        this.handlerExecutor = handlerExecutor;
    }

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        handlerExecutor = new HandlerExecutor();
        handlerMappings.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        Object handler = handlerMappings.getHandler(request);
        try {
            ModelAndView modelAndView = handlerExecutor.execute(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
