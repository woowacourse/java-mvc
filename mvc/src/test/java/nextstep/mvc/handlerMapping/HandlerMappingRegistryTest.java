package nextstep.mvc.handlerMapping;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import samples.TestController;
import samples.TestHandlerMapping;

class HandlerMappingRegistryTest {

    @Test
    void getHandler() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new TestHandlerMapping());
        handlerMappingRegistry.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertAll(
            () -> assertThat(handler).isNotEmpty(),
            () -> assertThat(handler.get()).isInstanceOf(TestController.class)
        );
    }

    @Test
    void cantGetHandler() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test_will_fail");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isEmpty();
    }
}
