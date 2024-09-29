package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.interface21.context.container.Container;

import samples.TestApp;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry sut;

    @BeforeEach
    void setUp() {
        Container.run(TestApp.class);
        sut = new HandlerMappingRegistry();
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("Find handler mapping.")
    void getHandler(String path, String method, Class<?> expected) {
        // given
        var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRequestURI()).thenReturn(path);

        // when
        var actual = sut.getHandler(request);

        // then
        assertThat(actual).isInstanceOf(expected);
    }

    static Stream<Arguments> getHandler() {
        return Stream.of(
                Arguments.of("/get-test", "GET", HandlerExecution.class),
                Arguments.of("/post-test", "POST", HandlerExecution.class),
                Arguments.of("/put-test", "PUT", HandlerExecution.class),
                Arguments.of("/delete-test", "DELETE", HandlerExecution.class)
        );
    }

    @Test
    @DisplayName("Throw exception if failed to find handler mapping")
    void getHandler_fail() {
        // given
        var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("OPTIONS");
        when(request.getRequestURI()).thenReturn("/invalid-path");

        // when & then
        assertThatThrownBy(() -> sut.getHandler(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("No handler found for request URI");
    }
}
