package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    @DisplayName("handler에 따라 Adapter를 구분해서 가져온다")
    @Test
    void init() throws NoSuchMethodException {
        final Method method =
                TestController.class.getDeclaredMethod(
                        "findUserId",
                        HttpServletRequest.class,
                        HttpServletResponse.class);
        final HandlerExecution handler = new HandlerExecution(new TestController(), method);
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new ManualHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        final HandlerAdapter handlerAdaptor = handlerAdapterRegistry.getHandlerAdaptor(handler);

        assertAll(
                () -> assertThat(handlerAdaptor).isNotNull(),
                () -> assertThat(handlerAdaptor).isInstanceOf(AnnotationHandlerAdapter.class)
        );
    }
}