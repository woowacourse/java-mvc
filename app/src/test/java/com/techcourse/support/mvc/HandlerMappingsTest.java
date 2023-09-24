package com.techcourse.support.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webmvc.org.springframework.web.servlet.mvc.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.HandlerExecution;

class HandlerMappingsTest {

    private HandlerMappings handlerMappings;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerMappings = new HandlerMappings();
        handlerMappings.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.initialize();
        request = mock(HttpServletRequest.class);
    }

    @ParameterizedTest
    @CsvSource({"/logout, GET", "/register/view, GET", "/register, POST", "/login, POST", "/login/view, GET"})
    void getHandlerFromAnnotationHandlerMapping(String requestURI, String method) {
        // given
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getMethod()).thenReturn(method);

        // when
        Object handler = handlerMappings.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
