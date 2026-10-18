package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void 존재하지_않는_사용자를_조회할_경우_예외_응답을_반환한다() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("non-exist");

        ModelAndView modelAndView = userController.show(request, response);

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        assertThat(modelAndView.getObject("message")).isEqualTo("존재하지 않는 사용자입니다.");
    }

}
