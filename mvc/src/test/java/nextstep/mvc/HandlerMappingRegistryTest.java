package nextstep.mvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    @Test
    @DisplayName("init 메서드가 호출되면 HandlerMapping의 init 메서드가 호출된다.")
    void init() {
        final HandlerMapping handlerMapping1 = mock(HandlerMapping.class);
        final HandlerMapping handlerMapping2 = mock(HandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping1);
        handlerMappingRegistry.addHandlerMapping(handlerMapping2);

        handlerMappingRegistry.init();

        verify(handlerMapping1, times(1)).initialize();
        verify(handlerMapping2, times(1)).initialize();
    }

    @Test
    @DisplayName("요청이 들어오면 그에 맞는 핸들러를 찾아온다.")
    void getHandler() throws NoSuchMethodException {
        final HttpServletRequest request = mock(HttpServletRequest.class);

        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappingRegistry.addHandlerMapping(handlerMapping);
        handlerMappingRegistry.init();

        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/get-test");
        final Method expected = TestController.class.getDeclaredMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class
        );

        final Object actual = handlerMappingRegistry.getHandler(request);

        assertThat((HandlerExecution) actual)
                .extracting("method")
                .isEqualTo(expected);
    }
}