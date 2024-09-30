package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("HandlerAdapterRegistry에서 지원하는 핸들러이면 해당 HandlerAdapter를 반환한다.")
    void should_return_AnnotationHandlerAdapter_when_HandlerExecution() {
        // given
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when & then
        assertThat(handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                .isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @Test
    @DisplayName("지원하는 타입의 HandlerAdapter가 존재하지 않으면 예외가 발생한다.")
    void should_throw_exception_when_handlerAdapter_that_can_handle_not_exist() {
        // given
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        Controller controller = mock(Controller.class);

        // when & then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(controller))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%s handler를 지원하는 HandlerAdapter가 존재하지 않습니다."
                        .formatted(controller.getClass().getName()));
    }
}
