package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerExecutionAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @Test
    @DisplayName("핸들러를 받으면 그 핸들러를 실행한다.")
    void getHandlerAdapter() throws Exception {
        final HandlerAdapter handlerAdapter = new HandlerExecutionAdapter();
        handlerAdapterRegistry.addHandlerAdapter(handlerAdapter);

        final Method executionMethod = TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );
        final Object controller = executionMethod.getDeclaringClass().getConstructor().newInstance();
        final HandlerExecution handlerExecution = new HandlerExecution(controller, executionMethod);
        final HandlerAdapter findHandlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        assertThat(findHandlerAdapter)
                .isInstanceOf(HandlerExecutionAdapter.class);
    }
}
