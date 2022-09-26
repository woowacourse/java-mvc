package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("핸들러 매핑을 찾는다.")
    void getHandlerMapping() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        final Object handlerMapping = handlerMappingRegistry.getHandlerMapping(request);

        assertThat(handlerMapping).isNotNull();
    }
}
