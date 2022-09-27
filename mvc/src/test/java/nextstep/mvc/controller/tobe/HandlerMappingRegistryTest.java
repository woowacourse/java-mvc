package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("request에 대응하는 핸들러를 정상 반환")
    @Test
    void getHandler() {
        final var registry = new HandlerMappingRegistry();
        final var handlerMapping = new AnnotationHandlerMapping("samples");
        registry.add(handlerMapping);

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertDoesNotThrow(() -> registry.getHandler(request));
    }

    @DisplayName("request에 대응하는 핸들러가 없다면 예외 발생")
    @Test
    void getHandler_throw_noSuchElementException() {
        final var registry = new HandlerMappingRegistry();
        final var handlerMapping = new AnnotationHandlerMapping("nextstep.mvc.controller.tobe.fixture");
        registry.add(handlerMapping);

        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/other");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> registry.getHandler(request))
                .isInstanceOf(NoSuchElementException.class);
    }
}
