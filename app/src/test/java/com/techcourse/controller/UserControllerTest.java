package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest extends ControllerTest {

    @DisplayName("user를 json으로 응답한다.")
    @Test
    void show() {
        // given
        UserController userController = new UserController();

        // when
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("joanne");

        final ModelAndView modelAndView = userController.show(request, response);

        // then
        assertThat(modelAndView.view()).isInstanceOf(JsonView.class);
        assertThat(modelAndView.model()).containsKey("user");
    }
}
