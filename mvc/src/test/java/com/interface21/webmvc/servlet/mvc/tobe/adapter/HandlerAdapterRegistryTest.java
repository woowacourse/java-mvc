package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    @Test
    void 요청을_처리할_수_있는_어댑터를_반환() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        HandlerAdapterRegistry handlerAdapterRegistry = HandlerAdapterRegistry.getInstance();
        handlerAdapterRegistry.addHandlerAdapter(annotationHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(manualHandlerAdapter);

        // when
        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(actual).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @Test
    void 요청을_처리할_수_있는_어댑터가_없다면_null을_반환() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        HandlerAdapterRegistry handlerAdapterRegistry = HandlerAdapterRegistry.getInstance();
        handlerAdapterRegistry.addHandlerAdapter(manualHandlerAdapter);

        // when
        HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(actual).isNull();
    }
}
