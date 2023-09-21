package com.techcourse.support.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

class HandlerAdaptersTest {

    private HandlerAdapters handlerAdapters;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handlerAdapters = new HandlerAdapters();
        handlerAdapters.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapters.addHandlerAdapter(new AnnotationHandlerAdapter());
        request = mock(HttpServletRequest.class);
    }

    @ParameterizedTest
    @CsvSource({"/", "/logout", "/register/view", "/register"})
    void 매뉴얼_핸들러_매핑의_핸들러를_받으면_매뉴얼_핸들러_어댑터를_반환한다(String requestURI) {
        // given
        when(request.getRequestURI()).thenReturn(requestURI);
        HandlerMapping handlerMapping = new ManualHandlerMapping();
        handlerMapping.initialize();
        Object handler = handlerMapping.getHandler(request);

        // when
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(ManualHandlerAdapter.class);
    }

    @ParameterizedTest
    @CsvSource({"/login, POST", "/login/view, GET"})
    void 어노테이션_핸들러_매핑의_핸들러를_받으면_어노테이션_핸들러_어댑터를_반환한다(String requestURI, String method) {
        // given
        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getMethod()).thenReturn(method);
        HandlerMapping handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
        Object handler = handlerMapping.getHandler(request);

        // when
        HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }
}
