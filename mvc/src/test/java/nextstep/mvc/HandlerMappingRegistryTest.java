package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    private HandlerMappingRegistry handlerMappingRegistry;

    @BeforeEach
    void setup() {
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();
    }

    @DisplayName("요청에 맞는 핸들러를 찾는다.")
    @Test
    void findHandler() {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        //when
        Optional<Object> handler = handlerMappingRegistry.findHandler(request);

        //then
        assertThat(handler.orElseThrow().getClass()).isEqualTo(HandlerExecution.class);
    }

    @DisplayName("요청에 맞는 핸들러가 없을 시 예외가 발생한다.")
    @Test
    void findHandler_Exception() {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        //when then
        assertThat(handlerMappingRegistry.findHandler(request)).isEmpty();
    }
}
