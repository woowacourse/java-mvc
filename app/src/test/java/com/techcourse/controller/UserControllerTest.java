package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private final UserController controller = new UserController();

    @DisplayName("/api/user 로 GET 요청 시")
    @Nested
    class UserGet {

        @DisplayName("일치하는 회원이 존재할 경우, id, account, email 정보가 응답된다")
        @Test
        void return_user_info_if_matched_account_exists() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            given(request.getRequestURI()).willReturn("/api/user");
            given(request.getParameter("account")).willReturn("gugu");

            // when
            final ModelAndView modelAndView = controller.show(request, response);

            // then
            assertThat(modelAndView)
                    .extracting("model")
                    .extracting("user")
                    .usingRecursiveComparison()
                    .isEqualTo(new User(1L, "gugu", "password", "hkkang@woowahan.com"));
        }
    }
}
