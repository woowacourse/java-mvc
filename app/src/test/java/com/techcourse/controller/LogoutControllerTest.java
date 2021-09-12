package com.techcourse.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogoutControllerTest {

    @Test
    @DisplayName("로그아웃 테스트")
    void logoutTest() {

        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        final HttpServletResponse response = mock(HttpServletResponse.class);

        LogoutController controller = new LogoutController();

        // when
        final String viewName = controller.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("redirect:/");
    }
}
