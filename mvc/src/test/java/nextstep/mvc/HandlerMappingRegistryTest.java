package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void initialize() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(
                List.of(new AnnotationHandlerMapping("samples"))
        );
        handlerMappingRegistry.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isNotNull();
    }

    @Test
    void addHandlerMapping() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry(new ArrayList<>());
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        Object handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isNotNull();
    }
}
