package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerMappingAdapter());
    }

    @Test
    @DisplayName("HandlerExecution으로 AnnotationHandlerAdapter를 반환할 수 있다.")
    void should_return_AnnotationHandlerAdapter_when_HandlerExecution() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when & then
        assertThat(handlerAdapterRegistry.getHandlerAdapter(handlerExecution))
                .isInstanceOf(AnnotationHandlerMappingAdapter.class);
    }

    @Test
    @DisplayName("지원하는 타입의 HandlerAdapter가 존재하지 않으면 예외가 발생한다.")
    void should_throw_exception_when_handlerAdapter_that_can_handle_not_exist() {
        // given
        Controller controller = mock(Controller.class);

        // when & then
        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(controller))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("%s handler를 지원하는 HandlerAdapter가 존재하지 않습니다."
                        .formatted(controller.getClass().getName()));
    }
}
