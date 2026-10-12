package com.techcourse.controller;

import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@DisplayName("사용자 조회 컨트롤러")
class UserControllerTest {

    private UserController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        controller = new UserController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getParameter("account")).thenReturn("gugu");
    }

    @Nested
    @DisplayName("사용자를 찾았을 때")
    class FoundUser {

        @Test
        @DisplayName("요청의 계정으로 조회한 사용자를 모델에 담는다")
        void addsFoundUserToModel() {
            // given
            final var user = new User(1, "gugu", "password", "gugu@example.com");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(Optional.of(user));

                // when
                final var modelAndView = controller.show(request, response);

                // then
                assertThat(modelAndView.getObject("user")).isSameAs(user);
            }
        }

        @Test
        @DisplayName("JSON 뷰를 선택한다")
        void selectsJsonView() {
            // given
            final var user = new User(1, "gugu", "password", "gugu@example.com");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(Optional.of(user));

                // when
                final var modelAndView = controller.show(request, response);

                // then
                assertThat(modelAndView.getView()).isInstanceOf(JsonView.class);
            }
        }
    }

    @Nested
    @DisplayName("사용자를 찾지 못했을 때")
    class MissingUser {

        @Test
        @DisplayName("조회 결과가 없으면 예외를 던진다")
        void throwsWhenUserIsMissing() {
            // given
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> controller.show(request, response))
                        .isInstanceOf(NoSuchElementException.class);
            }
        }
    }
}
