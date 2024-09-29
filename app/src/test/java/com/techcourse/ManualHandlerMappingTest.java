package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerMappingTest {

    @Test
    @DisplayName("문자열이 주어지면 해당하는 컨트롤러를 반환한다.")
    void getHandler() {
        // given
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/login");
        given(request.getMethod()).willReturn("GET");

        // when
        final var handler = manualHandlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(LoginController.class);
    }

    @Test
    @DisplayName("컨트롤러가 없는 경우 예외를 던진다.")
    void getHandlerWithNoController() {
        // given
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/not-exist");
        given(request.getMethod()).willReturn("GET");

        // when & then
        assertThatThrownBy(() -> manualHandlerMapping.getHandler(request))
                .isInstanceOf(ControllerNotFoundException.class);
    }
}
