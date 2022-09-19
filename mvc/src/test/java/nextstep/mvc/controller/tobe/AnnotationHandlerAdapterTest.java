package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

    @DisplayName("AnnotationHandlerAdapter 은 HandlerExecution 에 대한 처리를 지원한다.")
    @Test
    void supportsTrue() {
        HandlerExecution handlerExecution = new HandlerExecution(null, null);
        boolean supports = annotationHandlerAdapter.supports(handlerExecution);

        assertThat(supports).isTrue();
    }

    @DisplayName("AnnotationHandlerAdapter 은 HandlerExecution 외엔 지원하지 않는다.")
    @Test
    void supportsFalse() {
        boolean supports = annotationHandlerAdapter.supports(new Object());

        assertThat(supports).isFalse();
    }
}