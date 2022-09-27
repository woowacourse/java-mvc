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

class LoginControllerTest {

    private final ControllerTestEnv controllerTestEnv = new ControllerTestEnv();

    @BeforeEach
    void init() {
        controllerTestEnv.init();
    }

    @Test
    void login_success() throws ServletException, IOException {
        final HttpServletRequest request = getLoginRequest("gugu", "password");
        final HttpServletResponse response = controllerTestEnv.getResponse();

        controllerTestEnv.sendRequest(request, response);

        verify(response, times(1)).sendRedirect("/index.jsp");
    }

    @Test
    void login_fail() throws ServletException, IOException {
        final HttpServletRequest request = getLoginRequest("gugu", "wrong");
        final HttpServletResponse response = controllerTestEnv.getResponse();

        controllerTestEnv.sendRequest(request, response);

        verify(response, times(1)).sendRedirect("/401.jsp");
    }

    private HttpServletRequest getLoginRequest(final String account, final String password) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("account", account);
        parameter.put("password", password);

        final HttpServletRequest request = controllerTestEnv.getRequestOf("/login", "POST", parameter);
        controllerTestEnv.setRequestSessionForLogin(request);

        return request;
    }
}