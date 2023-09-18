package com.techcourse;

import com.techcourse.exception.HandlerAdapterNotFoundException;
import com.techcourse.exception.HandlerNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final List<HandlerMapping> handlerMappings;
    private static final List<HandlerAdapter> handlerAdapters;

    static {
        handlerMappings = new ArrayList<>();
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping());

        handlerAdapters = new ArrayList<>();
        handlerAdapters.add(new ManualHandlerAdapter());
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = handlerMappings.stream()
                    .map(handlerMapping -> handlerMapping.getHandler(request))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElseThrow(() -> new HandlerNotFoundException(
                            "해당하는 Handler를 찾을 수 없습니다."
                                    + " URI : " + request.getMethod()
                                    + " METHOD : " + request.getMethod()));

            HandlerAdapter adapter = handlerAdapters.stream()
                    .filter(handlerAdapter -> handlerAdapter.supports(handler))
                    .findFirst()
                    .orElseThrow(() -> new HandlerAdapterNotFoundException(
                            "해당 Handler를 처리할 수 있는 HandlerAdapter를 찾을 수 없습니다."
                    ));

            ModelAndView modelAndView = adapter.handle(handler, request, response);

            View view = modelAndView.getView();
            Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (HandlerNotFoundException | HandlerAdapterNotFoundException e) {
            log.error(e.getMessage());
            response.setStatus(404);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
