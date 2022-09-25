package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void set() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }


    @Test
    @DisplayName("핸들러 레지스트리를 초기화한다.")
    void init() {
        final AnnotationHandlerMapping handlerMapping = mock(AnnotationHandlerMapping.class);
        handlerMappingRegistry.addHandlerMapping(handlerMapping);

        handlerMappingRegistry.init();

        verify(handlerMapping).initialize();
    }

    @Test
    @DisplayName("핸들러를 등록한다.")
    void addHandlerMapping() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.init();

        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler.get()).isNotNull();
    }

    @Test
    @DisplayName("일치하는 핸들러가 없는 경우 Optional Empty를 반환한다.")
    void addHandlerMapping_notFound() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/not-found");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        handlerMappingRegistry.init();

        final Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler).isEqualTo(Optional.empty());
    }

}
