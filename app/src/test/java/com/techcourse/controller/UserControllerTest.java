package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private final UserController userController = new UserController();

    @Test
    @DisplayName("유효한 계정을 조회한다.")
    void 유효_계정_조회_테스트() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final String validAccount = "gugu";
        final User expect = InMemoryUserRepository.findByAccount(validAccount).get();
        when(request.getParameter("account")).thenReturn(validAccount);

        // when
        final ModelAndView modelAndView = userController.show(request, response);
        final User actual = (User) modelAndView.getModel().get("user");

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("유효하지 않은 계정을 조회하면 Exception이 발생한다.")
    void 유효하지_않은_계정_조회_테스트() {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final String invalidAccount = "invalid account";
        when(request.getParameter("account")).thenReturn(invalidAccount);

        // when & then
        assertThatThrownBy(() -> userController.show(request, response))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
