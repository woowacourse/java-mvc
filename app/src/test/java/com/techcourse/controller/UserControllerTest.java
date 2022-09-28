package com.techcourse.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        // given
        Map<String, String> parameter = new HashMap<>();
        parameter.put("account", "gugu");

        final HttpServletRequest request = controllerTestEnv.getRequestOf("/api/user", "GET", parameter);
        final HttpServletResponse response = controllerTestEnv.getResponse();
        controllerTestEnv.setWriterForJsonResponse(response);

        // when
        controllerTestEnv.sendRequest(request, response);

        // then
        controllerTestEnv.verifyResponseWrite(response,
                "{\"id\":1,\"account\":\"gugu\",\"password\":\"password\",\"email\":\"hkkang@woowahan.com\"}");
    }
}