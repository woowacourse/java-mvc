package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @Test
    void userController() {
        UserController userController = new UserController();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");
        ModelAndView modelAndView = userController.show(request, response);
        Map<String, Object> model = modelAndView.getModel();
        User actual = (User) model.get("user");

        assertThat(actual.getAccount()).isEqualTo("gugu");
    }
}
