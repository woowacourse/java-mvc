package com.techcourse.controller;

import com.techcourse.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private final UserController controller = new UserController();

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("파라미터에 해당하는 유저 반환 테스트")
    void showTest() {

        // given
        when(request.getParameter("account")).thenReturn("gugu");

        try (MockedStatic<UserSession> session = mockStatic(UserSession.class)) {
            session.when(() -> UserSession.isLoggedIn(any())).thenReturn(true);

            // when
            final ModelAndView modelAndView = controller.show(request, response);

            // then
            final ModelAndView expected = new ModelAndView(new JsonView());
            final User user = new User(1, "gugu", "password", "hkkang@woowahan.com");
            expected.addObject("user", user);
            assertThat(modelAndView).usingRecursiveComparison().isEqualTo(expected);
        }
    }
}
