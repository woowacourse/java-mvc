package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @Test
    void ifGetUserThenReturnUserAndJsonView() {
        //given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");

        //when
        var result = new UserController().show(request, response);

        //then
        User user = (User) result.getObject("user");

        assertThat(user.getAccount()).isEqualTo("gugu");
        assertThat(result.getView()).isExactlyInstanceOf(JsonView.class);
    }
}
