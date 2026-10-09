package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Test
    void 존재하지_않는_계정이면_예외가_발생한다() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("nobody");
        final var userController = new UserController();

        assertThatThrownBy(() -> userController.show(request, response))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
