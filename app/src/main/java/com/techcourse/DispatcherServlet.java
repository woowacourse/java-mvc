package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.Handler;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    @Override
    public void init() {
        this.handlerMappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("com.techcourse"));

        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request,
                           final HttpServletResponse response) throws ServletException {
        final Handler handler = handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.supports(request))
                .findAny()
                .orElseThrow()
                .getHandler(request);

        handler.handle(request, response);
    }

}
