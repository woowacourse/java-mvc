package nextstep.mvc.support;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerMappingRegistryTest {

    @Test
    void 특정_요청을_처리할_핸들러를_불러온다() {
        // given
        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getMethod()).thenReturn("GET");

        // when

        final Optional<Object> wrappedHandler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(wrappedHandler).isPresent();
    }
}
