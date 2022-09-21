package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nextstep.mvc.controller.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LogoutControllerTest {

    private final Controller controller = new LogoutController();

    @DisplayName("/logout 요청 시 session.removeAttribute가 호출되고, viewName으로 redirect:/ 가 응답된다")
    @Test
    void logout_should_return_root_page_with_calling_removeAttribute() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HttpSession session = mock(HttpSession.class);
        given(request.getSession()).willReturn(session);

        // when
        final String viewName = controller.execute(request, response);

        // then
        assertAll(
                () -> verify(session, times(1)).removeAttribute("user"),
                () -> assertThat(viewName).isEqualTo("redirect:/")
        );
    }
}
