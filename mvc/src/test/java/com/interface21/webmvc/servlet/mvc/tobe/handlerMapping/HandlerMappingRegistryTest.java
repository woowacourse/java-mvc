package com.interface21.webmvc.servlet.mvc.tobe.handlerMapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry sut;

    @BeforeEach
    void setUp() {
        sut = new HandlerMappingRegistry();
        sut.addHandlerMapping(new AnnotationHandlerMapping("samples"));
    }

    @Test
    @DisplayName("처리 가능한 핸들러를 반환한다.")
    void getHandler() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        var actual = sut.getHandler(request);

        assertThat(actual).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("처리 가능한 핸들러가 없는 경우 예외를 던진다.")
    void getHandler_null() {
        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");

        assertThatThrownBy(() ->sut.getHandler(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No handler found");
    }
}
