package com.techcourse.controller.api;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UserController 테스트")
class UserControllerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserController userController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        userController = new UserController();
    }

    @DisplayName("UserController GET /api/user 테스트 - 회원이 존재할 때")
    @Test
    void getWhenAccountExists() {
        // given
        final String gugu = "gugu";
        final User user = new User(1L, gugu, "password", "hkkang@woowahan.com");

        when(request.getParameter("account")).thenReturn(gugu);

        // when
        final ModelAndView modelAndView = userController.show(request, response);
        final Map<String, Object> model = modelAndView.getModel();

        // then
        assertThat(model).containsEntry("user", user);
    }

    @DisplayName("UserController GET /api/user 테스트 - 회원이 존재하지 않을 때")
    @Test
    void getWhenAccountNotExists() {
        // given
        final String inbi = "inbi";

        when(request.getParameter("account")).thenReturn(inbi);

        // when
        final ModelAndView modelAndView = userController.show(request, response);
        final View view = modelAndView.getView();

        // then
        assertThat(view).isInstanceOf(JspView.class);
    }
}