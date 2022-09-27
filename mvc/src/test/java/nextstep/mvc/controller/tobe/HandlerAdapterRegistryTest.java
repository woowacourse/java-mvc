package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.exception.HandlerAdapterNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    @DisplayName("적절한 HandlerAdapter를 반환한다.")
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

    @DisplayName("적절한 HandlerAdapter가 없을 경우 예외를 발생시킨다.")
    @Test
    void getHandlerAdapter_HandlerAdapterNotFoundException() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
        Object invalidHandler = Integer.MAX_VALUE;

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(invalidHandler))
                .isInstanceOf(HandlerAdapterNotFoundException.class);
    }
}
