package nextstep.mvc;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;

class HandlerMappingRegistryTest {

    @Test
    void getHandler() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new HandlerForTest());
        handlerMappingRegistry.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertAll(
            () -> assertThat(handler).isNotEmpty(),
            () -> assertThat(handler.get()).isInstanceOf(ControllerForTest.class)
        );
    }

    @Test
    void cantGetHandler() {
        // given
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new HandlerForTest());
        handlerMappingRegistry.init();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test_will_fail");

        // when
        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        // then
        assertThat(handler).isEmpty();
    }

    class HandlerForTest implements HandlerMapping {
        @Override
        public void initialize() {

        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            if (request.getRequestURI().equals("/test")) {
                return new ControllerForTest();
            }
            return null;
        }
    }

    class ControllerForTest implements Controller {
        @Override
        public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            return null;
        }
    }
}
