package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @Test
    void parameter로_user를_찾을_수_있다() {
        // given
        UserController userController = new UserController();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("gugu");

        // when
        ModelAndView mav = userController.show(request, response);

        // then
        Assertions.assertAll(
                () -> assertThat(mav.getView()).isInstanceOf(JsonView.class),
                () -> assertThat(mav.getObject("user")).isInstanceOf(User.class)
        );
    }
}
