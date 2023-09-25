package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserController userController = new UserController();
    @Test
    void account가_없으면_예외가_발생한다() {
        //given
        final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        when(mockRequest.getParameter("account")).thenReturn(null);

        //when
        //then
        assertThatThrownBy(() -> userController.show(mockRequest, mockResponse))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("account를 입력해주세요.");
    }
}
