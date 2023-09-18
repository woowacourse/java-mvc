package com.techcourse.support.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

class ManualHandlerMappingTest {

    private HandlerMapping handlerMapping;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
    }

    @ParameterizedTest
    @CsvSource({"/", "/logout", "/register", "/register/view"})
    void getHandler(String requestURI) {
        when(request.getRequestURI()).thenReturn(requestURI);

        Object handler = handlerMapping.getHandler(request);
        assertThat(handler).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({"/login", "/login/view"})
    void getHandlerFail(String requestURI) {
        when(request.getRequestURI()).thenReturn(requestURI);

        Object handler = handlerMapping.getHandler(request);
        assertThat(handler).isNull();
    }
}
