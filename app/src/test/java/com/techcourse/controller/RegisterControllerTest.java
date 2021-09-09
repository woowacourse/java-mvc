package com.techcourse.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterControllerTest {

    @Test
    @DisplayName("회원가입 테스트")
    void registerTest() {

        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter(anyString())).thenReturn("test");

        final HttpServletResponse response = mock(HttpServletResponse.class);

        RegisterController controller = new RegisterController();

        // when
        final String viewName = controller.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("redirect:/index.jsp");
    }
}
