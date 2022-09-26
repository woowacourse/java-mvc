package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @Test
    void show() {
        UserController userController = new UserController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("gugu");

        ModelAndView modelAndView = userController.show(request, response);
        User user = (User) modelAndView.getObject("user");

        assertThat(user.getAccount()).isEqualTo("gugu");
    }
}
