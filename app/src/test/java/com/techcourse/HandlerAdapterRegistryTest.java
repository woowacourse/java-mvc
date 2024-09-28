package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry sut = new HandlerAdapterRegistry();

    @ParameterizedTest
    @MethodSource
    @DisplayName("Find handler adapter.")
    void getHandler(Class<?> handlerType, Class<?> adapterType) {
        // given
        var handler = mock(handlerType);

        // when
        var actual = sut.getHandlerAdapter(handler);

        // then
        assertThat(actual).isInstanceOf(adapterType);
    }

    static Stream<Arguments> getHandler() {
        return Stream.of(
                Arguments.of(Controller.class, ManualHandlerAdaptor.class),
                Arguments.of(HandlerExecution.class, AnnotationHandlerAdapter.class));
    }

    @Test
    @DisplayName("Throw exception if failed to find handler adapter.")
    void getHandler_fail() {
        // given
        var unsupportedHandler = new Object();

        // when & then
        assertThatThrownBy(() -> sut.getHandlerAdapter(unsupportedHandler))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageStartingWith("No handler adapter found for");
    }
}
