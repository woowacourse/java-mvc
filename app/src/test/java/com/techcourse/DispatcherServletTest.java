package com.techcourse;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.ServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import static org.assertj.core.api.Assertions.assertThat;

class DispatcherServletTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
    }

    @Test
    void homeAndLoginAndRegistrationPagesAreForwarded() throws Exception {
        assertThat(service(new MockHttpServletRequest("GET", "/")).getForwardedUrl())
                .isEqualTo("/index.jsp");
        assertThat(service(new MockHttpServletRequest("GET", "/login/view")).getForwardedUrl())
                .isEqualTo("/login.jsp");
        assertThat(service(new MockHttpServletRequest("GET", "/register/view")).getForwardedUrl())
                .isEqualTo("/register.jsp");
        assertThat(service(new MockHttpServletRequest("GET", "/register")).getForwardedUrl())
                .isEqualTo("/register.jsp");
    }

    @Test
    void successfulLoginStoresUserInSession() throws Exception {
        final var request = loginRequest("gugu", "password");

        assertThat(service(request).getRedirectedUrl()).isEqualTo("/index.jsp");
        assertThat(UserSession.getUserFrom(request.getSession()).orElseThrow().getAccount())
                .isEqualTo("gugu");
    }

    @Test
    void incorrectPasswordDoesNotLogIn() throws Exception {
        final var request = loginRequest("gugu", "wrong-password");

        assertThat(service(request).getRedirectedUrl()).isEqualTo("/401.jsp");
        assertThat(UserSession.isLoggedIn(request.getSession())).isFalse();
    }

    @Test
    void unknownAccountDoesNotLogIn() throws Exception {
        final var request = loginRequest("unknown-account", "password");

        assertThat(service(request).getRedirectedUrl()).isEqualTo("/401.jsp");
        assertThat(UserSession.isLoggedIn(request.getSession())).isFalse();
    }

    @Test
    void loggedInUserIsRedirectedFromLoginPageAndLoginRequest() throws Exception {
        final var session = loggedInSession();
        final var viewRequest = new MockHttpServletRequest("GET", "/login/view");
        viewRequest.setSession(session);
        final var loginRequest = new MockHttpServletRequest("POST", "/login");
        loginRequest.setSession(session);

        assertThat(service(viewRequest).getRedirectedUrl()).isEqualTo("/index.jsp");
        assertThat(service(loginRequest).getRedirectedUrl()).isEqualTo("/index.jsp");
    }

    @Test
    void logoutRemovesUserFromSession() throws Exception {
        final var request = new MockHttpServletRequest("GET", "/logout");
        request.setSession(loggedInSession());

        assertThat(service(request).getRedirectedUrl()).isEqualTo("/");
        assertThat(UserSession.isLoggedIn(request.getSession())).isFalse();
    }

    @Test
    void registeredUserCanLogInAndBeRetrievedAsJson() throws Exception {
        final var request = new MockHttpServletRequest("POST", "/register");
        request.setParameter("account", "step3-user");
        request.setParameter("password", "secret");
        request.setParameter("email", "step3@example.com");

        assertThat(service(request).getRedirectedUrl()).isEqualTo("/index.jsp");
        assertThat(service(loginRequest("step3-user", "secret")).getRedirectedUrl())
                .isEqualTo("/index.jsp");

        final var apiRequest = new MockHttpServletRequest("GET", "/api/user");
        apiRequest.setParameter("account", "step3-user");
        final var response = service(apiRequest);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(response.getContentAsString())
                .contains("\"account\":\"step3-user\"", "\"email\":\"step3@example.com\"")
                .doesNotContain("\"user\":", "secret", "password");
    }

    @Test
    void unmappedPathReturnsNotFound() throws Exception {
        assertThat(service(new MockHttpServletRequest("GET", "/missing")).getStatus()).isEqualTo(404);
    }

    @Test
    void wrongHttpMethodDoesNotExecuteController() throws Exception {
        assertThat(service(new MockHttpServletRequest("GET", "/login")).getStatus()).isEqualTo(404);
        assertThat(service(new MockHttpServletRequest("POST", "/api/user")).getStatus()).isEqualTo(404);
    }

    private MockHttpServletResponse service(final MockHttpServletRequest request) throws Exception {
        final var response = new MockHttpServletResponse();
        servlet.service((ServletRequest) request, response);
        return response;
    }

    private MockHttpServletRequest loginRequest(final String account, final String password) {
        final var request = new MockHttpServletRequest("POST", "/login");
        request.setParameter("account", account);
        request.setParameter("password", password);
        return request;
    }

    private MockHttpSession loggedInSession() {
        final var session = new MockHttpSession();
        final User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        session.setAttribute(UserSession.SESSION_KEY, user);
        return session;
    }
}
