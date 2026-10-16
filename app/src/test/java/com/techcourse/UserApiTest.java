package com.techcourse;

import com.interface21.web.http.MediaType;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserApiTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter body;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new DispatcherServlet();
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        body = new StringWriter();
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    @DisplayName("사용자 조회 결과를 모델 키 없이 JSON으로 응답한다")
    void returnsUserAsJson() throws Exception {
        when(request.getParameter("account")).thenReturn("gugu");

        servlet.service(request, response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"gugu\"}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    @DisplayName("한글 계정도 JSON으로 응답한다")
    void returnsUnicodeAccount() throws Exception {
        final String account = "재키-" + UUID.randomUUID();
        InMemoryUserRepository.save(new User(3, account, "password", "jackie@example.com"));
        when(request.getParameter("account")).thenReturn(account);

        servlet.service(request, response);

        assertThat(body.toString()).isEqualTo("{\"account\":\"" + account + "\"}");
    }

    @Test
    @DisplayName("사용자 조회에 매핑하지 않은 POST 요청은 404로 응답한다")
    void rejectsUnmappedHttpMethod() throws Exception {
        when(request.getMethod()).thenReturn("POST");

        servlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 조회 실패 원인을 보존한다")
    void preservesMissingUserFailure() {
        when(request.getParameter("account")).thenReturn("missing-" + UUID.randomUUID());

        assertThatThrownBy(() -> servlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasRootCauseInstanceOf(NoSuchElementException.class);
    }
}
