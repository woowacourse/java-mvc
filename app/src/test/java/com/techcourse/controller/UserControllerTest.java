package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @DisplayName("존재하는 계정이면 유저의 정보를 반환한다.")
    @Test
    void 존재하는_계정이면_유저의_정보를_반환한다() {
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

    @DisplayName("존재하지 않는 계정이면 오류 페이지를 반환한다")
    @Test
    void 존재하지_않는_계정이면_오류_페이지를_반환한다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("Yaho");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");

        UserController userController = new UserController();
        ModelAndView modelAndView = userController.show(request, response);

        View view = modelAndView.getView();
        String actual = view.toString();

        assertThat(actual).contains("redirect:/404.jsp");
    }

    @DisplayName("계정정보에 null 값이 들어오면 오류 페이지를 반환한다")
    @Test
    void 계정정보에_null_값이_들어오면_오류_페이지를_반환한다() {
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");

        UserController userController = new UserController();
        ModelAndView modelAndView = userController.show(request, response);

        View view = modelAndView.getView();
        String actual = view.toString();

        assertThat(actual).contains("redirect:/404.jsp");
    }
}
