package com.techcourse.controller;

import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Test
    @DisplayName("account로 조회한 사용자를 모델에 담고 JsonView를 반환한다")
    void show() {
        final var account = UUID.randomUUID().toString();
        final var user = new User(2, account, "password", "user@example.com");
        InMemoryUserRepository.save(user);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn(account);

        final var modelAndView = new UserController().show(request, response);

        assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
        assertThat(modelAndView.getModel()).containsOnlyKeys("user");
        assertThat(modelAndView.getObject("user")).isSameAs(user);
    }

    @Test
    @DisplayName("존재하지 않는 account로 조회하면 예외가 발생한다")
    void showWithUnknownAccount() {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn(UUID.randomUUID().toString());

        assertThatThrownBy(() -> new UserController().show(request, response))
                .isInstanceOf(NoSuchElementException.class);
    }
}
