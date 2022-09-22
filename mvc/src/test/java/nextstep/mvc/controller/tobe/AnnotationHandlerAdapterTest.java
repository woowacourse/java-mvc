package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    @Test
    @DisplayName("handler의 클래스 타입이 HandlerExecution이라면 support의 결과값이 true이다.")
    void supports_true() {
        // given
        HandlerExecution handler = mock(HandlerExecution.class);

        // when
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        boolean actual = annotationHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("handler의 클래스 타입이 HandlerExecution이 아니라면 support의 결과값이 false이다.")
    void supports_false() {
        // given
        Object handler = new Object();

        // when
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        boolean actual = annotationHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isFalse();
    }
}
