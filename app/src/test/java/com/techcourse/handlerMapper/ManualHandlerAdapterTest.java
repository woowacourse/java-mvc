package com.techcourse.handlerMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerAdapterTest {

    private ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
    private ManualHandlerMapping manualHandlerMapping;

    @BeforeEach
    void setUp() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
    }

    @Test
    void Controller_타입의_핸들러_처리가능() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/");

        Object handler = manualHandlerMapping.getHandler(request);

        assertThat(manualHandlerAdapter.supports(handler)).isTrue();
    }

    @Test
    void handle_메소드_실행시_ModelAndView_반환() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/");

        Object handler = manualHandlerMapping.getHandler(request);

        assertThat(manualHandlerAdapter.handle(request, response, handler)).isInstanceOf(ModelAndView.class);
    }

}
