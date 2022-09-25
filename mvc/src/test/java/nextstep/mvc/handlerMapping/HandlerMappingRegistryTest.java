package nextstep.mvc.handlerMapping;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.exception.ServletException;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("적절한 handlerExecution을 찾는다.")
    void getHandler() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/get-test");

        Object result = handlerMappingRegistry.getHandler(httpServletRequest);
        assertThat(result.getClass()).isEqualTo(HandlerExecution.class);
    }

    @Test
    @DisplayName("적절한 handlerExecution이 없을 시 에러를 발생시킨다.")
    void getHandlerError() {
        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.initialize();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/error");

        assertThatThrownBy(() -> handlerMappingRegistry.getHandler(httpServletRequest))
            .isInstanceOf(ServletException.class)
            .hasMessage("handler를 찾을 수 없습니다.");
    }
}