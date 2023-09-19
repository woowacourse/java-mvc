package com.techcourse.handlerMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
    private AnnotationHandlerMapping annotationHandlerMapping;

    @BeforeEach
    void setUp() {
        annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
    }

    @Test
    void HandlerExecution_타입의_핸들러_처리가능() {
        final var request = mock(HttpServletRequest.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = annotationHandlerMapping.getHandler(request);

        assertThat(annotationHandlerAdapter.supports(handler)).isTrue();
    }

    @Test
    void handle_메소드_실행시_ModelAndView_반환() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        Object handler = annotationHandlerMapping.getHandler(request);

        assertThat(annotationHandlerAdapter.handle(request, response, handler)).isInstanceOf(ModelAndView.class);
    }

}
