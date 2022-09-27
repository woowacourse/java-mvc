package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import nextstep.mvc.view.JsonView;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    @Test
    void user() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getMethod()).thenReturn("GET");
        when(response.getWriter()).thenReturn(writer);
        doNothing().when(writer).write(anyString());

        UserController userController = new UserController();

        final var modelAndView = userController.show(request, response);
        modelAndView.render(request, response);
        final var view = modelAndView.getView();

        assertThat(view).isInstanceOf(JsonView.class);
        verify(response).getWriter();
    }
}
