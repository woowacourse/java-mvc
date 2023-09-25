package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

class UserControllerTest {

    @Test
    void show() {
        // given
        final UserController userController = new UserController();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter(any())).thenReturn("홍고");
        final User user = new User(2, "홍고", "패스워드", "hongo@admin.com");

        // when
        final ModelAndView mav = userController.show(request, response);

        // then
        assertThat(mav.getObject("user")).isEqualTo(user);
    }
}
