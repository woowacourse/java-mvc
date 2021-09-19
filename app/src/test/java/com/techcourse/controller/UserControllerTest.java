package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("유저 컨트롤러 테스트")
class UserControllerTest {
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @DisplayName("Get 요청 시")
    @Nested
    class UserGet {

        @DisplayName("유저정보를 담은 jsonView가 리턴된다")
        @Test
        void userGetTest() {
            //given
            when(request.getParameter("account")).thenReturn("gugu");

            UserController userController = new UserController();
            //when
            ModelAndView mav = userController.show(request);

            //then
            User user = (User) mav.getObject("user");
            assertThat(user.account().value()).isEqualTo("gugu");
            assertThat(mav.getView()).isInstanceOf(JsonView.class);
        }
    }
}