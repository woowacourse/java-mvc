package nextstep.mvc.handlerAdapter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.mvc.controller.tobe.HandlerExecution;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("Handler Execution의 구현체라면 true를 반환한다.")
    void supportsWhenTrue() {
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        HandlerExecution HandlerExecution = new HandlerExecution(null, null);
        assertThat(handlerAdapter.supports(HandlerExecution)).isTrue();
    }

    @Test
    @DisplayName("Handler Execution의 구현체가 아니라면 false를 반환한다.")
    void supportsWhenFalse() {
        AnnotationHandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        Object object = new Object();
        assertThat(handlerAdapter.supports(object)).isFalse();
    }

}
