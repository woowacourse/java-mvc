package com.techcourse.controller.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @DisplayName("등록되어 있는 user 정보를 알 수 있다")
    @Test
    void show() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        UserController userController = new UserController();

        when(request.getParameter(eq("account"))).thenReturn("gugu");

        ModelAndView actual = userController.show(request, response);
        User user = (User) actual.getObject("user");

        assertThat(user.getAccount()).isEqualTo("gugu");
    }
}
