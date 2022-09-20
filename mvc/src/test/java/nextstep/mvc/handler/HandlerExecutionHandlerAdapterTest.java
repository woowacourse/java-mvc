package nextstep.mvc.handler;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerExecutionHandlerAdapterTest {

    private static final Object DUMMY_TARGET = new Object();

    HandlerExecutionHandlerAdapter sut;

    @BeforeEach
    void setUp() {
        sut = new HandlerExecutionHandlerAdapter();
    }

    @Test
    void supportHandlerExecutionInstance() throws NoSuchMethodException {
        Object DUMMY_TARGET = new Object();
        Method DUMMY_METHOD = DUMMY_TARGET.getClass().getMethod("wait");
        HandlerExecution handlerExecution = new HandlerExecution(DUMMY_TARGET, DUMMY_METHOD);

        boolean actual = sut.supports(handlerExecution);

        assertThat(actual).isTrue();
    }
}
