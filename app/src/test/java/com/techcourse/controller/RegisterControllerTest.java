package com.techcourse.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegisterControllerTest {

    private final ControllerTestEnv controllerTestEnv = new ControllerTestEnv();

    @BeforeEach
    void init() {
        controllerTestEnv.init();
    }

    @Test
    void register() throws ServletException, IOException {
        final HttpServletRequest request = getRegisterRequest();
        final HttpServletResponse response = controllerTestEnv.getResponse();

        controllerTestEnv.sendRequest(request, response);

        verify(response, times(1)).sendRedirect("/index.jsp");
    }

    private HttpServletRequest getRegisterRequest() {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("account", "eve");
        parameter.put("password", "password");
        parameter.put("email", "eve@woowa.net");

        return controllerTestEnv.getRequestOf("/register", "POST", parameter);
    }
}