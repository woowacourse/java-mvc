package com.techcourse.support.web.handler.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

class HandlerMappingsTest {

    private static final HandlerMappings handlerMappings = new HandlerMappings();

    @BeforeAll
    static void setUp() {
        handlerMappings.initialize();
    }

    @Test
    void testGetHandler() {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");

        //when
        final Object handler = handlerMappings.getHandler(request);

        //then
        assertThat(handler).isInstanceOf(Controller.class);
    }
}
