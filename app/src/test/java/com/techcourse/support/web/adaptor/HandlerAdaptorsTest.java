package com.techcourse.support.web.adaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.adaptor.AnnotationHandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.adaptor.HandlerAdaptors;
import webmvc.org.springframework.web.servlet.mvc.mapping.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.adaptor.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.mapping.HandlerMapping;

class HandlerAdaptorsTest {

    private final HandlerAdaptors handlerAdaptors = new HandlerAdaptors();

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
        assertThat(adaptor).isInstanceOf(AnnotationHandlerAdaptor.class);
    }
}
