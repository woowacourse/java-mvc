package nextstep.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("HandlerMapping을 등록하고 handler를 꺼내온다.")
    void getHandler() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        willReturn("/get-test")
                .given(request)
                .getRequestURI();
        willReturn("GET")
                .given(request)
                .getMethod();

        final HandlerMappingRegistry registry = new HandlerMappingRegistry();
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        // when
        registry.addHandlerMapping(handlerMapping);
        final Object handler = registry.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }
}
