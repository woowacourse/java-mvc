package nextstep.mvc.controller.tobe.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.HandlerMapping;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    void initialize() {
        HandlerMappingRegistry registry = new HandlerMappingRegistry(new ArrayList<>());

        final HandlerMapping handlerMapping1 = mock(HandlerMapping.class);
        final HandlerMapping handlerMapping2 = mock(HandlerMapping.class);

        registry.addHandlerMapping(handlerMapping1);
        registry.addHandlerMapping(handlerMapping2);

        registry.initialize();

        verify(handlerMapping1, times(1)).initialize();
        verify(handlerMapping2, times(1)).initialize();
    }

    @Test
    void addHandlerMapping() throws Exception {
        HandlerMappingRegistry registry = new HandlerMappingRegistry(new ArrayList<>());

        registry.addHandlerMapping(new AnnotationHandlerMapping());

        Field field = registry.getClass()
                .getDeclaredField("handlerMappings");
        field.setAccessible(true);

        assertThat((List<?>) field.get(registry)).hasSize(1);
    }

    @Test
    void getHandler() {
        HandlerMappingRegistry registry = new HandlerMappingRegistry(new ArrayList<>());

        registry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        registry.initialize();

        final var request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertThat(registry.getHandler(request).orElseThrow()).isNotNull();
    }
}
