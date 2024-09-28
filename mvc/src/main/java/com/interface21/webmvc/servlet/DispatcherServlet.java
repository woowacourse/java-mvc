package com.interface21.webmvc.servlet;

import com.interface21.webmvc.HandlerMappings;
import com.interface21.webmvc.RequestMappingHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapters;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.view.JsonViewResolver;
import com.interface21.webmvc.servlet.view.JspViewResolver;
import com.interface21.webmvc.servlet.view.ViewResolvers;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<String> scanPackages;
    private HandlerMappings handlerMappings;
    private HandlerAdapters handlerAdapters;
    private ViewResolvers viewResolvers;

    public DispatcherServlet(String... basePackage) {
        scanPackages = new ArrayList<>();
        scanPackages.add(getClass().getPackageName());
        scanPackages.addAll(Arrays.stream(basePackage).toList());
    }

    @Override
    public void init() {
        handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping(scanPackages));
        handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new RequestMappingHandlerAdapter());
        viewResolvers = new ViewResolvers();
        viewResolvers.addViewResolvers(new JsonViewResolver());
        viewResolvers.addViewResolvers(new JspViewResolver());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerMapping handlerMapping = handlerMappings.getHandlerMapping(request);
            Object handler = handlerMapping.getHandler(request);

            HandlerAdapter adapter = handlerAdapters.getHandlerAdapter(handler);
            ModelAndView modelAndView = adapter.handle(handler, request, response);

            View view = viewResolvers.resolveViewName(modelAndView.getViewName());
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
