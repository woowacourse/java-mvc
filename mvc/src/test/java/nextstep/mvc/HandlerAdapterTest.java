package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import nextstep.mvc.adapter.AnnotationHandlerAdapter;
import nextstep.mvc.adapter.ControllerHandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterTest {

    @DisplayName(value = "AnnotationHandlerAdapter가 지원하는 handler면 true")
    @Test
    void AnnotationHandlerAdapterTrue() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final Object handler = mock(HandlerExecution.class);

        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @DisplayName(value = "AnnotationHandlerAdapter가 지원하지 않는 handler면 false")
    @Test
    void AnnotationHandlerAdapterFalse() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final Object handler = new Object();

        // when & then
        assertThat(handlerAdapter.supports(handler)).isFalse();
    }

    @DisplayName(value = "ControllerHandlerAdapter가 지원하는 handler면 true")
    @Test
    void ControllerHandlerAdapterTrue() {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final Object handler = mock(Controller.class);

        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @DisplayName(value = "ControllerHandlerAdapter가 지원하지 않는 handler면 false")
    @Test
    void ControllerHandlerAdapterFalse() {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final Object handler = new Object();

        // when & then
        assertThat(handlerAdapter.supports(handler)).isFalse();
    }
}
