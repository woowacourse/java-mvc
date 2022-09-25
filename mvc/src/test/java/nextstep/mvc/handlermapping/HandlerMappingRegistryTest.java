package nextstep.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.web.support.RequestMethod;

class HandlerMappingRegistryTest {

    @DisplayName("annotation 기반 handler mapping 초기화")
    @Test
    void annotationInit() {
        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        registry.add(new AnnotationHandlerMapping("samples"));
        registry.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn(String.valueOf(RequestMethod.GET));

        assertThat(registry.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }
}
