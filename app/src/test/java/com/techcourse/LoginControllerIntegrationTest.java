package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class LoginControllerIntegrationTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
    }

    @Test
    @DisplayName("로그인에 성공하면 세션에 사용자를 저장하고 메인 화면으로 이동한다")
    void loginSuccessStoresUserInSession() throws Exception {
        final var request = loginRequest("gugu", "password");
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        final var user = (User) request.getSession().getAttribute(UserSession.SESSION_KEY);
        assertThat(user.getAccount()).isEqualTo("gugu");
        assertThat(response.getRedirectedUrl()).isEqualTo("/index.jsp");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 세션에 저장하지 않고 401 화면으로 이동한다")
    void wrongPasswordRedirectsTo401() throws Exception {
        final var request = loginRequest("gugu", "wrong-password");
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        assertThat(request.getSession().getAttribute(UserSession.SESSION_KEY)).isNull();
        assertThat(response.getRedirectedUrl()).isEqualTo("/401.jsp");
    }

    @Test
    @DisplayName("존재하지 않는 계정이면 401 화면으로 이동한다")
    void unknownAccountRedirectsTo401() throws Exception {
        final var request = loginRequest("nobody", "password");
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        assertThat(request.getSession().getAttribute(UserSession.SESSION_KEY)).isNull();
        assertThat(response.getRedirectedUrl()).isEqualTo("/401.jsp");
    }

    @Test
    @DisplayName("이미 로그인한 사용자가 다시 로그인하면 메인 화면으로 이동한다")
    void alreadyLoggedInRedirectsToIndex() throws Exception {
        final var session = loggedInSession();
        final var request = loginRequest("gugu", "wrong-password");
        request.setSession(session);
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        assertThat(response.getRedirectedUrl()).isEqualTo("/index.jsp");
    }

    @Test
    @DisplayName("로그아웃하면 세션에서 사용자를 제거하고 루트로 이동한다")
    void logoutRemovesUserFromSession() throws Exception {
        final var session = loggedInSession();
        final var request = new MockHttpServletRequest("GET", "/logout");
        request.setSession(session);
        final var response = new MockHttpServletResponse();

        servlet.service(request, response);

        assertThat(session.getAttribute(UserSession.SESSION_KEY)).isNull();
        assertThat(response.getRedirectedUrl()).isEqualTo("/");
    }

    private MockHttpServletRequest loginRequest(final String account, final String password) {
        final var request = new MockHttpServletRequest("POST", "/login");
        request.addParameter("account", account);
        request.addParameter("password", password);
        return request;
    }

    private MockHttpSession loggedInSession() {
        final var session = new MockHttpSession();
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        session.setAttribute(UserSession.SESSION_KEY, user);
        return session;
    }
}
