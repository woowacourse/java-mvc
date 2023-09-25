package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

class UserControllerTest {

    @DisplayName("account 값을 통해 메모리에 저장한 사용자의 정보를 파악할 수 있다.")
    @Test
    void show() {
        // given
        final UserController userController = new UserController();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final User user = new User(99L, "gr", "ay", "hello@gray.com");
        InMemoryUserRepository.save(user);

        when(request.getParameter("account")).thenReturn(user.getAccount());

        // when
        final ModelAndView modelAndView = userController.show(request, response);

        // then
        assertThat(modelAndView.getObject("user")).isEqualTo(user);
    }
}
