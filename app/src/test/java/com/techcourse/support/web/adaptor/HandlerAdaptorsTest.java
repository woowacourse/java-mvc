package com.techcourse.support.web.adaptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.support.web.mapping.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

class HandlerAdaptorsTest {

    private final HandlerAdaptors handlerAdaptors = new HandlerAdaptors();

    @BeforeEach
    void setup() {
        handlerAdaptors.initialize();
    }

    @Test
    @DisplayName("handler 타입에 맞는 Adaptor를 반환한다.")
    void getManualAdaptor() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        Object handler = manualHandlerMapping.getHandler(request);

        //when
        HandlerAdaptor adaptor = handlerAdaptors.getAdaptor(handler);

        //then
        Assertions.assertThat(adaptor).isInstanceOf(ManualHandlerAdaptor.class);
    }

    @Test
    @DisplayName("handler 타입에 맞는 Adaptor를 반환한다.")
    void getAnnotationAdaptor() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");

        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("sample");
        annotationHandlerMapping.initialize();

        Object handler = annotationHandlerMapping.getHandler(request);

        //when
        HandlerAdaptor adaptor = handlerAdaptors.getAdaptor(handler);

        //then
        Assertions.assertThat(adaptor).isInstanceOf(AnnotationHandlerAdaptor.class);
    }
}