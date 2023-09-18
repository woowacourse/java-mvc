package com.techcourse.support.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

class HandlerMappingsTest {

    @ParameterizedTest
    @CsvSource({"/logout", "/register/view", "/register"})
    void getHandlerFromManualHandlerMapping(String requestURI) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(requestURI);

        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.initialize();

        Object handler = handlerMappings.getHandler(request);
        assertThat(handler).isInstanceOf(Controller.class);
    }

    @ParameterizedTest
    @CsvSource({"/login, POST", "/login/view, GET"})
    void getHandlerFromAnnotationHandlerMapping(String requestURI, String method) {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getMethod()).thenReturn(method);

        HandlerMappings handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new ManualHandlerMapping());
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.initialize();

        Object handler = handlerMappings.getHandler(request);
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
