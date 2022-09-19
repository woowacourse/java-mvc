package nextstep.mvc.handleradaptor;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    @Test
    void 핸들러가_HandlerExecution의_객체일_때_핸들러_어댑터를_찾아온다()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // given
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        final ManualHandlerAdaptor manualHandlerAdaptor = new ManualHandlerAdaptor();
        handlerAdapterRegistry.addHandlerAdapter(handlerExecutionHandlerAdapter);
        handlerAdapterRegistry.addHandlerAdapter(manualHandlerAdaptor);

        final HandlerExecution handlerExecution = new HandlerExecution(
                TestController.class.getConstructor().newInstance(),
                TestController.class.getDeclaredMethod(
                        "findUserId", HttpServletRequest.class, HttpServletResponse.class
                )
        );
        // when
        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(handlerAdapter.getClass()).isEqualTo(HandlerExecutionHandlerAdapter.class);
    }
}
