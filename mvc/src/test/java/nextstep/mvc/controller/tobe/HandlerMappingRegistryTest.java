package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import nextstep.mvc.controller.tobe.handlermapping.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.handlermapping.HandlerMappingRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class HandlerMappingRegistryTest {

    @DisplayName("request에 맞는 Handler를 반환한다.")
    @Test
    void getHandler() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isPresent();
    }
}
