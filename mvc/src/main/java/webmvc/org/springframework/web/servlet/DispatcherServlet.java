package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    public static final String ANNOTATION_BASE_PACKAGE = "com.techcourse.controller";
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.add(new AnnotationHandlerMapping(ANNOTATION_BASE_PACKAGE));
        handlerMappings.forEach(HandlerMapping::initialize);

        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            ModelAndView modelAndView = execute(request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private ModelAndView execute(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        Object handler = getHandler(request);
        HandlerAdapter adapter = getHandlerAdapter(handler);
        return adapter.handle(request, response, handler);
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        HandlerAdapter adapter = handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 요청입니다."));
        return adapter;
    }

    private Object getHandler(HttpServletRequest request) {
        Object handler = handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.isMatch(request))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 요청입니다."))
                .getHandler(request);
        return handler;
    }
}
