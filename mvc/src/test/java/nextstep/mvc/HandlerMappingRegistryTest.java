package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setUp() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Test
    void handlerMapping을_추가한다() {
        handlerMappingRegistry.add(new AnnotationHandlerMapping("nextstep.mvc"));
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/stub");
        when(request.getMethod()).thenReturn("GET");

        handlerMappingRegistry.initialize();
        final Optional<Object> handler = handlerMappingRegistry.findHandler(request);

        assertThat(handler).isPresent();
    }

    @Test
    void 핸들러가_존재하지_않으면_Optional_empty를_반환한다() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/stub");
        when(request.getMethod()).thenReturn("GET");

        handlerMappingRegistry.initialize();
        final Optional<Object> handler = handlerMappingRegistry.findHandler(request);

        assertThat(handler).isEmpty();
    }
}
