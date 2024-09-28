package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingAdapter;
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
        }; // TODO: reflection 이용?
        Arrays.stream(handlerMappings).forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerMapping handlerMapping = selectHandlerMapping(request);
            ModelAndView modelAndView = handlerMapping.execute(request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerMapping selectHandlerMapping(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.containsRequest(request)) {
                return handlerMapping;
            }
        }
        throw new IllegalArgumentException("해당 요청을 처리하는 핸들러 매퍼가 없습니다: %s %s"
                .formatted(request.getMethod(), request.getRequestURI()));
    }
}
