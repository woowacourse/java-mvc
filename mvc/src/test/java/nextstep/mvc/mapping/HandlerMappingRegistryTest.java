package nextstep.mvc.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.AnnotationHandlerMapping;
import nextstep.mvc.controller.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("요청에 따라 알맞은 handler를 찾는다.")
    @Test
    void getHandler() {
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertThat(handlerMappingRegistry.getHandler(request)).isInstanceOf(HandlerExecution.class);
    }
}
