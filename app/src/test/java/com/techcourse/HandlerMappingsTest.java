package com.techcourse;

import com.techcourse.controller.LoginViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.org.springframework.web.bind.annotation.RequestMethod;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingsTest {

    private HandlerMappings handlerMappings;

    @BeforeEach
    void setUp() {
        handlerMappings = new HandlerMappings();
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse"));
    }

    @Test
    void ManualHandlerMapping에서_적절한_핸들러를_찾아온다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/login/view");

        assertThat(handlerMappings.getHandler(request)).isInstanceOf(LoginViewController.class);
    }

    @Test
    void AnnotationHandlerMapping에서_적절한_핸들러를_찾아온다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/ano/login");
        when(request.getMethod()).thenReturn(String.valueOf(RequestMethod.GET));

        assertThat(handlerMappings.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void request에_맞는_핸들러를_발견_못했을때_예외_발생() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/notExist");
        when(request.getMethod()).thenReturn(String.valueOf(RequestMethod.GET));

        assertThatThrownBy(() -> handlerMappings.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
