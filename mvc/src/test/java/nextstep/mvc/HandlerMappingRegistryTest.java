package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerMappingRegistryTest {

    @DisplayName("요청에 따라 Handler 를 구분해서 가져온다")
    @Test
    void init() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI())
                .willReturn("/get-test");
        given(request.getMethod())
                .willReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();
        final Object handler = handlerMappingRegistry.getHandler(request);

        assertAll(
                () -> assertThat(handler).isNotNull(),
                () -> assertThat(handler).isInstanceOf(HandlerExecution.class)
        );
    }
}