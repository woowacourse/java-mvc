package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    @Test
    void getHandlerAdapter() throws NoSuchMethodException {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        TestController testController = new TestController();
        Class<TestController> clazz = TestController.class;
        Method method = clazz.getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class);
        Object handler = new HandlerExecution(testController, method);

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(handlerAdapter).isExactlyInstanceOf(AnnotationHandlerAdapter.class);
    }
}