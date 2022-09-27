package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter annotationHandlerAdapter;

    @BeforeEach
    void setUp() {
        annotationHandlerAdapter = new AnnotationHandlerAdapter();
    }

    @Test
    void supportsHandlerExecution() {
        final var handlerExecution = mock(HandlerExecution.class);
        assertThat(annotationHandlerAdapter.supports(handlerExecution)).isTrue();
    }
}
