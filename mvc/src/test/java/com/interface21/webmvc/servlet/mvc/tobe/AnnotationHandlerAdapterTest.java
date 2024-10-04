package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @Test
    void support() {
        // given
        HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution handlerExecution = new HandlerExecution(null, null);

        // when
        boolean actual = handlerAdapter.support(handlerExecution);

        // then
        assertThat(actual).isTrue();
    }
}
