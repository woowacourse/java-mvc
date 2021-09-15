package com.techcourse.session;

import com.techcourse.domain.User;
import com.techcourse.exception.NotFoundException;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.techcourse.session.UserSession.SESSION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("UserSession 테스트")
class UserSessionTest {

    private HttpSession session;
    private User user;

    @BeforeEach
    void setUp() {
        session = mock(HttpSession.class);
        user = new User(2L, "inbi", "password", "inbi@email.com");
    }

    @DisplayName("session에서 User 가져오기 테스트 - 존재하는 경우")
    @Test
    void getUserFrom() {
        // given
        when(session.getAttribute(SESSION_KEY)).thenReturn(user);

        // when
        final User sessionUser = UserSession.getUserFrom(session)
                .orElseThrow(() -> new NotFoundException("session에 User가 존재하지 않습니다."));

        // then
        assertThat(sessionUser).isEqualTo(user);
    }

    @DisplayName("session에서 User 가져오기 테스트 - 존재하지 않는 경우")
    @Test
    void getUserFromWhenNotExists() {
        // given
        when(session.getAttribute(SESSION_KEY)).thenReturn(null);

        // when
        final Optional<User> sessionUserOptional = UserSession.getUserFrom(session);

        // then
        assertThat(sessionUserOptional).isEmpty();
    }

    @DisplayName("로그인 상태 테스트 - 로그인 상태인 경우")
    @Test
    void isLoggedIn() {
        // given
        when(session.getAttribute(SESSION_KEY)).thenReturn(user);

        // when
        // then
        assertThat(UserSession.isLoggedIn(session)).isTrue();
    }

    @DisplayName("로그인 상태 테스트 - 비로그인 상태인 경우")
    @Test
    void isNotLoggedIn() {
        // given
        when(session.getAttribute(SESSION_KEY)).thenReturn(null);

        // when
        // then
        assertThat(UserSession.isLoggedIn(session)).isFalse();
    }
}