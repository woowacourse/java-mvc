package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        final List<HandlerMapping> handlerMappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping("com.techcourse")
        );
        handlerMappingRegistry = new HandlerMappingRegistry(handlerMappings);
    }

    @Test
    void 회원가입_GET_요청에_대한_handlerMapping를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        assertThat(handlerMapping.getClass()).isEqualTo(AnnotationHandlerMapping.class);
    }

    @Test
    void 회원가입_POST_요청에_대한_handlerMapping를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");

        final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        assertThat(handlerMapping.getClass()).isEqualTo(AnnotationHandlerMapping.class);
    }

    @Test
    void 로그인_POST_요청에_대한_handlerMapping를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");

        final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        assertThat(handlerMapping.getClass()).isEqualTo(AnnotationHandlerMapping.class);
    }

    @Test
    void 로그인_GET_요청에_대한_handlerMapping를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        assertThat(handlerMapping.getClass()).isEqualTo(AnnotationHandlerMapping.class);
    }

    @Test
    void 로그아웃_POST_요청에_대한_handlerMapping를_반환한다() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("POST");

        final HandlerMapping handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        assertThat(handlerMapping.getClass()).isEqualTo(AnnotationHandlerMapping.class);
    }
}
