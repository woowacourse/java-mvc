package nextstep.mvc.handlerAdapter;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.mvc.controller.tobe.HandlerExecution;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("HandlerExecution의 구현체라면 true를 return한다.")
    void handlerSupportsHandlerExecution() {
        HandlerExecution handlerExecution = mock(HandlerExecution.class);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        assertThat(annotationHandlerAdapter.supports(handlerExecution)).isTrue();
    }

    @Test
    @DisplayName("HandlerExecution의 구현체가 아니라면 false를 return한다.")
    void handlerNotSupport() {
        Object controller = mock(Object.class);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        assertThat(annotationHandlerAdapter.supports(controller)).isFalse();
    }
}
