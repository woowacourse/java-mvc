package nextstep.mvc.handler;

import static org.assertj.core.api.Assertions.*;

import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    HandlerExecutionHandlerAdapter sut;

    @BeforeEach
    void setUp() {
        sut = new HandlerExecutionHandlerAdapter();
    }

    @Test
    void supportHandlerExecutionInstance() {
        HandlerExecution handlerExecution = new HandlerExecution(null, null);

        boolean actual = sut.supports(handlerExecution);

        assertThat(actual).isTrue();
    }
}
