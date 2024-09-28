package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

class HandlerAdapterRegistryTest {

    @ParameterizedTest
    @MethodSource
    @DisplayName("Find handler adapter.")
    void getHandler(Class<?> handlerType) {
        // given
        var handlerExecution = mock(handlerType);
        var sut = new HandlerAdapterRegistry();

        // when
        var actual = sut.getHandlerAdapter(handlerExecution);

        // then
        assertThat(actual).isNotNull();
    }

    static Stream<Class<?>> getHandler() {
        return Stream.of(Controller.class, HandlerExecution.class);
    }

    @Test
    @DisplayName("Throw exception if failed to find handler adapter.")
    void getHandler_fail() {
        // given
        var unsupportedHandler = new Object();
        var sut = new HandlerAdapterRegistry();

        // when & then
        assertThatThrownBy(() -> sut.getHandlerAdapter(unsupportedHandler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("No handler adapter found for");
    }
}
