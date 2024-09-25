package com.techcourse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = addHandlerMappings();
        handlerAdapters = addHandlerAdapters();
    }

    private List<HandlerMapping> addHandlerMappings() {
        List<HandlerMapping> handlerMappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping(getClass().getPackageName())
        );
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
        return handlerMappings;
    }

    private List<HandlerAdapter> addHandlerAdapters() {
        List<HandlerAdapter> handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ControllerHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
        return handlerAdapters;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = findHandler(request);
            HandlerAdapter adapter = findHandlerAdapter(handler);

            ModelAndView modelAndView = adapter.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest request) {
        return getHandlerMapping(request).getHandler(request);
    }

    private HandlerMapping getHandlerMapping(HttpServletRequest request) {
        return handlerMappings.stream()
                .filter(handlerMapping -> Objects.nonNull(handlerMapping.getHandler(request)))
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("요청을 처리할 수 없습니다."));
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("요청을 처리할 수 없습니다."));
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();

        view.render(model, request, response);
    }
}
