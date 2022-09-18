package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

    @Test
    void supportsTrue() {
        HandlerExecution handlerExecution = new HandlerExecution(null, null);
        boolean supports = annotationHandlerAdapter.supports(handlerExecution);

        assertThat(supports).isTrue();
    }

    @Test
    void supportsFalse() {
        boolean supports = annotationHandlerAdapter.supports(new Object());

        assertThat(supports).isFalse();
    }
}