package nextstep.mvc.adpater;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.adpater.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerAdapterRegistryTest {

    @Test
    @DisplayName("객체를 처리할 수 있는 HandlerAdapter를 반환한다.")
    void getHandlerAdapter() throws NoSuchMethodException, ServletException {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final TestController controller = new TestController();
        final Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.add(handlerAdapter);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);

        // when
        final HandlerAdapter actual = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(actual).isEqualTo(handlerAdapter);
    }
}
