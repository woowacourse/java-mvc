package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.adapter.Adapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.adapter.HandlerExecutionAdapter;
import webmvc.org.springframework.web.servlet.mvc.mapper.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.mapper.Mapper;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;

    @Override
    public void init() {
        handlerMapping = handlerMapping();
        handlerMapping.initialize();
        handlerAdapter = handlerAdapter();
    }


    private HandlerMapping handlerMapping() {
        List<Mapper> mappers = new ArrayList<>();
        mappers.add(new AnnotationHandlerMapping("com.techcourse.controller"));
        return new HandlerMapping(mappers);
    }

    private HandlerAdapter handlerAdapter() {
        List<Adapter> adapters = new ArrayList<>();
        adapters.add(new HandlerExecutionAdapter());
        return new HandlerAdapter(adapters);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            final Object handler = handlerMapping.getHandler(request);
            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
