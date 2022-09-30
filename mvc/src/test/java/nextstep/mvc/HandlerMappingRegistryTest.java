package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new HandlerMappingRegistry();
    }

    @Test
    @DisplayName("핸들러 매핑이 정상적으로 추가된다.")
    void addHandlerMappingTest() {
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        registry.addHandlerMapping(handlerMapping);

        assertDoesNotThrow(() -> registry.getHandler(request));
    }
}