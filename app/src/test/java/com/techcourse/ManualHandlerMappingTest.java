package com.techcourse;

import com.techcourse.controller.LogoutController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManualHandlerMappingTest {

    @Test
    @DisplayName("요청 객체의 URI로 기존 컨트롤러를 찾는다")
    void findsLegacyControllerFromRequest() {
        // given
        final var mapping = new ManualHandlerMapping();
        mapping.initialize();
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getContextPath()).thenReturn("");

        // when
        final var handler = mapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(LogoutController.class);
    }

    @Test
    @DisplayName("컨텍스트 경로를 제외한 요청 경로로 기존 컨트롤러를 찾는다")
    void findsLegacyControllerWithinContextPath() {
        // given
        final var mapping = new ManualHandlerMapping();
        mapping.initialize();
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/mvc/logout");
        when(request.getContextPath()).thenReturn("/mvc");

        // when
        final var handler = mapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(LogoutController.class);
    }
}
