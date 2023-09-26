package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.DispatcherServletException.NotFoundHandlerAdapterException;
import webmvc.org.springframework.web.servlet.mvc.DispatcherServletException.NotFoundHandlerException;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handleradapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handleradapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.AnnotationHandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse.controller"));
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerMapping.initialize();
        }
        handlerAdapters.add(new AnnotationHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = getHandler(request);
            ModelAndView modelAndView = getModelAndView(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new DispatcherServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(NotFoundHandlerException::new);
    }

    private ModelAndView getModelAndView(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.support(handler))
                .map(handlerAdapter -> handlerAdapter.execute(handler, request, response))
                .findFirst()
                .orElseThrow(() -> new NotFoundHandlerAdapterException(handler.getClass().getSimpleName()));
    }
}
