package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @DisplayName("HandlerExecution을 support한다")
    @Test
    void supports() {
        HandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        assertThat(annotationHandlerAdapter.supports(handlerExecution)).isTrue();
    }
}
