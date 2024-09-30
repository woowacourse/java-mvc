package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

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
}
