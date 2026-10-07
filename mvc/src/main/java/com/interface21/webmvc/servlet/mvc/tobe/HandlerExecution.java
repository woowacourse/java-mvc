package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    /*
    
            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");
    
            final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            final var modelAndView = handlerExecution.handle(request, response);
    
            assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    
         */
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
//        id - "gugu", requestUri - "/get-test", method - "GET" 인 request 가 오면
//        id - "gugu" 인 modelAndView 를 리턴해야 한다.
//        이 handler 를 매핑해주는 건 handlerMapping 의 책임.
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        System.out.println(request.getAttributeNames().toString());
        request.getAttributeNames()
                .asIterator()
                .forEachRemaining(key -> {
                    Object value = request.getAttribute(key);
                    modelAndView.addObject(key, value);
                });
        return modelAndView;
    }
}
