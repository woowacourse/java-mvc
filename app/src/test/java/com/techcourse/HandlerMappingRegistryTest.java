package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.techcourse.controller.LoginController;
import com.techcourse.handlermapping.ManualHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @DisplayName("요청을 처리할 수 있는 HandlerMapping을 반환한다.")
    @Test
    void getHandler() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/login");

        HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(new ManualHandlerMapping());

        Optional<Object> handler = handlerMappingRegistry.getHandler(request);

        assertThat(handler.isPresent()).isTrue();
        assertThat(handler.get()).isInstanceOf(LoginController.class);
    }
}
