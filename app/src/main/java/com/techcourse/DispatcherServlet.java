package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.handleradapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.handleradapter.HandlerAdapter;
import com.interface21.webmvc.servlet.handleradapter.RequestMappingHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        try {
            manualHandlerMapping = new ManualHandlerMapping();
            manualHandlerMapping.initialize();

            annotationHandlerMapping = new AnnotationHandlerMapping(this.getClass().getPackageName());
            annotationHandlerMapping.initialize();

            handlerAdapters = List.of(new ControllerHandlerAdapter(), new RequestMappingHandlerAdapter());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = findHandler(request, requestURI);
            HandlerAdapter adapter = findHandlerAdapter(handler);

            ModelAndView modelAndView = adapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest request, String requestURI) {
        Object handler = manualHandlerMapping.getHandler(requestURI);
        if (handler == null) {
            return annotationHandlerMapping.getHandler(request);
        }
        return handler;
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(a -> a.supports(handler))
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("처리할 수 없는 요청입니다."));
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        if (!model.isEmpty()) {
            View view = modelAndView.getView();
            view.render(model, request, response);
        }
    }
}
