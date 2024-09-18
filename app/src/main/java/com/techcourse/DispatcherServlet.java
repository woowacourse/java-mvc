package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerHandlerAdaptor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdaptor;
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

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdaptor> handlerAdaptors;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdaptors();
    }

    private void initHandlerMappings() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        String packageName = getClass().getPackageName();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(packageName);
        annotationHandlerMapping.initialize();

        handlerMappings = List.of(annotationHandlerMapping, manualHandlerMapping);
    }

    private void initHandlerAdaptors() {
        handlerAdaptors = List.of(new HandlerExecutionHandlerAdaptor(), new ControllerHandlerAdaptor());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            Object handler = findHandler(request);
            HandlerAdaptor handlerAdaptor = findHandlerAdaptor(handler);
            ModelAndView modelAndView = handlerAdaptor.handle(request, response, handler);
            move(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object findHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            try {
                return handlerMapping.getHandler(request);
            } catch (IllegalArgumentException e) {
                log.info("{} 에 존재하지 않는 요청 : {} {}", handlerMapping.getClass().getName(),
                        request.getMethod(), request.getRequestURI());
            }
        }
        throw new IllegalArgumentException("지원하는 handlerMapping 이 없습니다.");
    }

    private HandlerAdaptor findHandlerAdaptor(Object handler) {
        return handlerAdaptors.stream()
                .filter(handlerAdaptor -> handlerAdaptor.support(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 handlerAdaptor 가 없습니다."));
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
