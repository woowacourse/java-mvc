package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @BeforeEach
    void setUp() {
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Test
    @DisplayName("애노테이션을 기반으로 하는 핸들러를 가져온다.")
    void getHandlerWithAnnotation() {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        Object handler = handlerMappingRegistry.getHandler(request);
        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isNotNull();
    }
}
