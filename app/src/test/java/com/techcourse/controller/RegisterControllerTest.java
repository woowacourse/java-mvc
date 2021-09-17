package com.techcourse.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

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
        final ModelAndView modelAndView = controller.execute(request, response);

        // then
        assertThat(modelAndView.getView()).usingRecursiveComparison().isEqualTo(new JspView("redirect:/index.jsp"));
    }
}
