package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
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
