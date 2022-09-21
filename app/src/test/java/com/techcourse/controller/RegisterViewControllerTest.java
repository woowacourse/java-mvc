package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RegisterViewControllerTest {

    private final Controller controller = new RegisterViewController();

    @DisplayName("/@mvc/register/view 요청 시 viewName으로 /register.jsp 가 응답된다")
    @Test
    void registerView_should_return_register_jsp() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        final String viewName = controller.execute(request, response);

        // then
        assertThat(viewName).isEqualTo("/register.jsp");
    }
}
