package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("회원가입 컨트롤러")
class RegisterControllerTest {

    private RegisterController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        controller = new RegisterController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Nested
    @DisplayName("GET /register")
    class ShowRegistrationForm {

        @Test
        @DisplayName("회원가입 JSP로 포워드한다")
        void forwardsToRegistrationPage() throws Exception {
            // given
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

            // when
            final var modelAndView = controller.show(request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);

            // then
            verify(dispatcher).forward(request, response);
        }

        @Test
        @DisplayName("화면을 보여줄 때 사용자를 저장하지 않는다")
        void doesNotSaveUser() {
            // given
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                controller.show(request, response);

                // then
                repository.verifyNoInteractions();
            }
        }
    }

    @Nested
    @DisplayName("POST /register")
    class SaveRegistration {

        @Test
        @DisplayName("요청의 계정, 비밀번호, 이메일로 사용자를 저장한다")
        void savesSubmittedUser() {
            // given
            when(request.getParameter("account")).thenReturn("new-user");
            when(request.getParameter("password")).thenReturn("secret");
            when(request.getParameter("email")).thenReturn("new@example.com");
            final var savedUser = ArgumentCaptor.forClass(User.class);

            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                controller.save(request, response);

                // then
                repository.verify(() -> InMemoryUserRepository.save(savedUser.capture()));
            }

            assertThat(savedUser.getValue())
                    .usingRecursiveComparison()
                    .isEqualTo(new User(2, "new-user", "secret", "new@example.com"));
        }

        @Test
        @DisplayName("가입을 마치면 인덱스 페이지로 리다이렉트한다")
        void redirectsToIndexAfterRegistration() throws Exception {
            // given
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                final var modelAndView = controller.save(request, response);
                modelAndView.getView().render(modelAndView.getModel(), request, response);

                // then
                verify(response).sendRedirect("/index.jsp");
            }
        }
    }
}
