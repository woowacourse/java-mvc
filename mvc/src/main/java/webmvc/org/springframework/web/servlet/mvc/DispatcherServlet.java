package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.HandlerRegistry;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.AnnotationHandlerMapping;

import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerRegistry handlerRegistry;

    public DispatcherServlet() {
        handlerRegistry = new HandlerRegistry();
    }

    @Override
    public void init() {
        handlerRegistry.addHandler(new AnnotationHandlerMapping("com.techcourse"), new AnnotationHandlerAdapter());
        handlerRegistry.initHandlers();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = handlerRegistry.findHandlerMapper(request);
            HandlerAdapter adapter = handlerRegistry.findHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.execute(request, response, handler);
            response(request, response, modelAndView);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void response(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView) throws Exception {
        Map<String, Object> model = modelAndView.getModel();

        modelAndView.getView()
                .render(model, request, response);
    }
}
