package com.techcourse.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private final ControllerTestEnv controllerTestEnv = new ControllerTestEnv();

    @BeforeEach
    void init() {
        controllerTestEnv.init();
    }

    @Test
    void user() throws IOException, ServletException {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("account", "gugu");

        final HttpServletRequest request = controllerTestEnv.getRequestOf("/api/user", "GET", parameter);
        final HttpServletResponse response = controllerTestEnv.getResponse();

        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        // when
        controllerTestEnv.sendRequest(request, response);

        // then
        verify(response.getWriter()).write(
                "{\"id\":1,\"account\":\"gugu\",\"password\":\"password\",\"email\":\"hkkang@woowahan.com\"}");
    }
}