package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionHandlerAdapterTest {

    @Test
    void support() throws Exception {
        final HandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        final TestController testController = new TestController();
        final Object handlerExecution = new HandlerExecution(testController, testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class));

        final boolean actual = handlerExecutionHandlerAdapter.supports(handlerExecution);

        assertThat(actual).isTrue();
    }
}
