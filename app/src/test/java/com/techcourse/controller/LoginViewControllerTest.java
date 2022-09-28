package com.techcourse.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginViewControllerTest {

    private final ControllerTestEnv controllerTestEnv = new ControllerTestEnv();

    @BeforeEach
    void init() {
        controllerTestEnv.init();
    }

    @Test
    void loginView() throws ServletException, IOException {
        // given
        final HttpServletRequest request = controllerTestEnv.getRequestOf("/login/view", "GET");
        final HttpServletResponse response = controllerTestEnv.getResponse();
        controllerTestEnv.setRequestSessionForLogin(request);

        // when
        controllerTestEnv.sendRequest(request, response);

        // then
        controllerTestEnv.verifyRequestForwardTo(request, "/login.jsp");
    }
}