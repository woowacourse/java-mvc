package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping[] handlerMappings;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = new HandlerMapping[]{
                new ManualHandlerMapping(),
                new HandlerMappingAdapter(new AnnotationHandlerMapping())
        };
        Arrays.stream(handlerMappings).forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);
            ModelAndView modelAndView = execute(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new IllegalArgumentException("해당 요청을 처리하는 핸들러가 없습니다: %s %s"
                .formatted(request.getMethod(), request.getRequestURI()));
    }

    private ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof Controller) {
            String viewName = ((Controller) handler).execute(request, response);
            return new ModelAndView(JspView.from(viewName));
        }
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }
        throw new IllegalArgumentException("해당 요청을 수행할 수 없습니다: %s %s"
                .formatted(request.getMethod(), request.getRequestURI()));
    }
}
