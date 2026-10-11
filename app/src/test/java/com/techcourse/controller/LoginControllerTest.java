package com.techcourse.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("로그인 컨트롤러")
class LoginControllerTest {

    private static final String ACCOUNT = "login-test-account";
    private static final String PASSWORD = "private-password-7a9";
    private static final String EMAIL = "private-7a9@example.test";
    private static final User USER = new User(42, ACCOUNT, PASSWORD, EMAIL);

    private LoginController controller;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        controller = new LoginController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
    }

    @Nested
    @DisplayName("로그 출력")
    class LoginLogging {

        private Logger logger;
        private Level previousLevel;
        private ListAppender<ILoggingEvent> appender;

        @BeforeEach
        void attachAppender() {
            logger = (Logger) LoggerFactory.getLogger(LoginController.class);
            previousLevel = logger.getLevel();
            logger.setLevel(Level.INFO);
            appender = new ListAppender<>();
            appender.start();
            logger.addAppender(appender);
        }

        @AfterEach
        void detachAppender() {
            logger.detachAppender(appender);
            appender.stop();
            logger.setLevel(previousLevel);
        }

        @Test
        @DisplayName("로그인 시도에서 계정을 기록한다")
        void logsAccountOnLoginAttempt() throws Exception {
            // given
            final var submittedPassword = PASSWORD;

            // when
            attemptLoginWith(submittedPassword);

            // then
            assertThat(loggedMessages())
                    .anySatisfy(message -> assertThat(message).contains(ACCOUNT));
        }

        @Test
        @DisplayName("로그인 시도에서 저장된 비밀번호를 기록하지 않는다")
        void doesNotLogStoredPassword() throws Exception {
            // given
            final var submittedPassword = PASSWORD;

            // when
            attemptLoginWith(submittedPassword);

            // then
            assertThat(loggedMessages())
                    .allSatisfy(message -> assertThat(message).doesNotContain(PASSWORD));
        }

        @Test
        @DisplayName("로그인 시도에서 이메일을 기록하지 않는다")
        void doesNotLogEmail() throws Exception {
            // given
            final var submittedPassword = PASSWORD;

            // when
            attemptLoginWith(submittedPassword);

            // then
            assertThat(loggedMessages())
                    .allSatisfy(message -> assertThat(message).doesNotContain(EMAIL));
        }

        @Test
        @DisplayName("인증 실패 시에도 저장된 비밀번호와 제출한 비밀번호를 기록하지 않는다")
        void doesNotLogEitherPasswordOnFailure() throws Exception {
            // given
            final var submittedPassword = "wrong-password-8b4";

            // when
            attemptLoginWith(submittedPassword);

            // then
            assertThat(loggedMessages())
                    .allSatisfy(message -> assertThat(message).doesNotContain(PASSWORD, submittedPassword));
        }

        private void attemptLoginWith(final String submittedPassword) throws Exception {
            when(request.getParameter("account")).thenReturn(ACCOUNT);
            when(request.getParameter("password")).thenReturn(submittedPassword);
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount(ACCOUNT))
                        .thenReturn(Optional.of(USER));
                controller.execute(request, response);
            }
        }

        private List<String> loggedMessages() {
            return appender.list.stream()
                    .map(ILoggingEvent::getFormattedMessage)
                    .toList();
        }
    }

    @Nested
    @DisplayName("인증 결과")
    class Authentication {

        @Test
        @DisplayName("비밀번호가 일치하면 인덱스 페이지로 리다이렉트한다")
        void redirectsToIndexOnSuccess() throws Exception {
            // given
            when(request.getParameter("account")).thenReturn(ACCOUNT);
            when(request.getParameter("password")).thenReturn(PASSWORD);
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount(ACCOUNT))
                        .thenReturn(Optional.of(USER));

                // when
                final var viewName = controller.execute(request, response);

                // then
                assertThat(viewName).isEqualTo("redirect:/index.jsp");
            }
        }

        @Test
        @DisplayName("비밀번호가 다르면 401 페이지로 리다이렉트한다")
        void redirectsToUnauthorizedOnWrongPassword() throws Exception {
            // given
            when(request.getParameter("account")).thenReturn(ACCOUNT);
            when(request.getParameter("password")).thenReturn("wrong-password");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount(ACCOUNT))
                        .thenReturn(Optional.of(USER));

                // when
                final var viewName = controller.execute(request, response);

                // then
                assertThat(viewName).isEqualTo("redirect:/401.jsp");
            }
        }

        @Test
        @DisplayName("존재하지 않는 계정이면 401 페이지로 리다이렉트한다")
        void redirectsToUnauthorizedForUnknownAccount() throws Exception {
            // given
            when(request.getParameter("account")).thenReturn(ACCOUNT);
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount(ACCOUNT))
                        .thenReturn(Optional.empty());

                // when
                final var viewName = controller.execute(request, response);

                // then
                assertThat(viewName).isEqualTo("redirect:/401.jsp");
            }
        }

        @Test
        @DisplayName("인증에 성공하면 세션에 사용자를 저장한다")
        void storesUserInSessionOnSuccess() throws Exception {
            // given
            when(request.getParameter("account")).thenReturn(ACCOUNT);
            when(request.getParameter("password")).thenReturn(PASSWORD);
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount(ACCOUNT))
                        .thenReturn(Optional.of(USER));

                // when
                controller.execute(request, response);

                // then
                verify(session).setAttribute(UserSession.SESSION_KEY, USER);
            }
        }

        @Test
        @DisplayName("이미 로그인했다면 인덱스 페이지로 리다이렉트한다")
        void redirectsToIndexWhenAlreadyLoggedIn() throws Exception {
            // given
            when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(USER);
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                // when
                final var viewName = controller.execute(request, response);

                // then
                assertThat(viewName).isEqualTo("redirect:/index.jsp");
            }
        }
    }
}
