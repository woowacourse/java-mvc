package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Optional;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("요청을 처리할 수 있는 Handler를 반환한다.")
    void getHandler() throws NoSuchMethodException {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HandlerMapping handlerMapping = mock(HandlerMapping.class);

        final TestController controller = new TestController();
        final Method method = controller.getClass()
                .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(controller, method);
        when(handlerMapping.getHandler(request)).thenReturn(handlerExecution);

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(handlerMapping);
        handlerMappingRegistry.initialize();

        // when
        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).hasValue(handlerExecution);
    }
}
