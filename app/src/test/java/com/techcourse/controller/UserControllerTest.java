package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @DisplayName("유저의 정보를 반환한다.")
    @Test
    void 유저의_정보를_반환한다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");

        UserController userController = new UserController();
        ModelAndView modelAndView = userController.show(request, response);

        User user = (User) modelAndView.getObject("user");

        assertThat(user.getAccount()).isEqualTo("gugu");
    }
}
