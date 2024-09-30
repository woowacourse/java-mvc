package com.techcourse.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    UserController userController = new UserController();

    @Test
    void show() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");

        ModelAndView modelAndView = userController.show(request, response);

        assertThat(modelAndView.getObject("user"))
                .isEqualTo(new User(1L, "gugu", "password", "hkkang@woowahan.com"));
    }
}
