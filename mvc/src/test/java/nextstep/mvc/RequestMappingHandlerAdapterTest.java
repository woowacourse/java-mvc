package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;
import samples.TestController;

class RequestMappingHandlerAdapterTest {

    private static final HandlerAdapter ADAPTER = new RequestMappingHandlerAdapter();

    @Test
    void supports() {
        Method[] methods = TestController.class.getDeclaredMethods();
        Method method = methods[0];

        Object handlerExecution = new HandlerExecution(new TestController(), method);

        assertTrue(ADAPTER.supports(handlerExecution));
    }

    @Test
    void supportsFalse() {
        assertFalse(ADAPTER.supports(new Object()));
    }
}
