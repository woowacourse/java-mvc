package com.techcourse.controller.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;

class UserControllerTest {

    private UserController userController;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("account에 해당하는 유저의 정보를 JsonView로 넘겨준다.")
    @Test
    void show() {
        // given
        when(request.getParameter("account")).thenReturn("gugu");

        // when
        ModelAndView modelAndView = userController.show(request, response);

        // then
        User user = (User) modelAndView.getModel().get("user");
        assertAll(
                () -> assertThat(modelAndView.getView()).isInstanceOf(JsonView.class),
                () -> assertThat(user.getAccount()).isEqualTo("gugu")
        );
    }
}
