package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.handleradapter.HandlerAdapter;
import com.interface21.webmvc.servlet.handleradapter.RequestMappingHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String basePackage;

    private AnnotationHandlerMapping annotationHandlerMapping;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        try {
            annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
            annotationHandlerMapping.initialize();

            handlerAdapters = List.of(new RequestMappingHandlerAdapter());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.info("Dispatcher Servlet을 초기화하던 중 오류가 발생했습니다. :: message = {}", e.getMessage(), e.getCause());
            throw new IllegalArgumentException(e);
        } catch (Exception e) {
            throw new RuntimeException("예상치 못한 예외가 발생했습니다.", e);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            Object handler = annotationHandlerMapping.getHandler(request);
            HandlerAdapter adapter = findHandlerAdapter(handler);

            ModelAndView modelAndView = adapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter findHandlerAdapter(Object handler) {
        return handlerAdapters.stream()
                .filter(a -> a.supports(handler))
                .findAny()
                .orElseThrow(() -> new UnsupportedOperationException("처리할 수 없는 요청입니다."));
    }

    private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
