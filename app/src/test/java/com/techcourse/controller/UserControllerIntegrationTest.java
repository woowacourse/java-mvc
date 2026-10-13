package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class UserControllerIntegrationTest {

    private DispatcherServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet("com.techcourse.controller");
        servlet.init();
    }

    @Test
    @DisplayName("존재하는 계정은 200과 사용자 JSON을 반환한다")
    void returnsOkWithUserJsonForExistingAccount() throws Exception {
        final String account = "user-" + UUID.randomUUID();
        InMemoryUserRepository.save(new User(2, account, "password", "user@example.com"));

        final MockHttpServletResponse response = getUser(account);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
        assertThat(response.getContentType()).isEqualTo("application/json;charset=UTF-8");
        assertThat(response.getCharacterEncoding()).isEqualTo(StandardCharsets.UTF_8.name());
        assertThat(response.getContentAsString()).isEqualTo("{\"account\":\"" + account + "\"}");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" \t\n"})
    @DisplayName("account가 누락되거나 빈 문자열·공백이면 400과 빈 응답 본문을 반환한다")
    void returnsBadRequestForInvalidAccount(final String account) throws Exception {
        final MockHttpServletResponse response = getUser(account);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
        assertThat(response.getContentAsByteArray()).isEmpty();
    }

    @Test
    @DisplayName("유효한 계정에 해당하는 사용자가 없으면 404와 빈 응답 본문을 반환한다")
    void returnsNotFoundWhenUserDoesNotExist() throws Exception {
        final MockHttpServletResponse response = getUser("missing-" + UUID.randomUUID());

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_NOT_FOUND);
        assertThat(response.getContentAsByteArray()).isEmpty();
    }

    private MockHttpServletResponse getUser(final String account) throws Exception {
        final var request = new MockHttpServletRequest("GET", "/api/user");
        final var response = new MockHttpServletResponse();
        if (account != null) {
            request.addParameter("account", account);
        }

        servlet.service((ServletRequest) request, response);
        return response;
    }
}
