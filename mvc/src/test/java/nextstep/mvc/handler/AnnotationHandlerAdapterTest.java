package nextstep.mvc.handler;

import static org.assertj.core.api.Assertions.*;

import nextstep.mvc.controller.tobe.HandlerExecution;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {

    AnnotationHandlerAdapter sut;

    @BeforeEach
    void setUp() {
        sut = new AnnotationHandlerAdapter();
    }

    @Test
    void supportHandlerExecutionInstance() {
        HandlerExecution handlerExecution = new HandlerExecution(null, null);

        boolean actual = sut.supports(handlerExecution);

        assertThat(actual).isTrue();
    }
}
