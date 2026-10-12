package com.techcourse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("사용자 API 요청의 MVC 연결")
class UserApiFlowTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Servlet servlet;

    @BeforeEach
    void setUp() throws ServletException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/api/user");
        servlet = new DispatcherServlet();
        servlet.init(mock(ServletConfig.class));
    }

    @Nested
    @DisplayName("GET /api/user?account=gugu")
    class GetUser {

        private StringWriter body;

        @BeforeEach
        void setUpGetRequest() throws Exception {
            when(request.getMethod()).thenReturn("GET");
            when(request.getParameter("account")).thenReturn("gugu");
            body = new StringWriter();
            when(response.getWriter()).thenReturn(new PrintWriter(body));
        }

        @Test
        @DisplayName("사용자의 id, account, email을 바깥 user 이름 없이 JSON으로 응답한다")
        void respondsWithUserData() throws Exception {
            // given
            final var user = new User(1, "gugu", "password", "gugu@example.com");
            final var expected = OBJECT_MAPPER.readTree("""
                    {"id":1,"account":"gugu","email":"gugu@example.com"}
                    """);
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(Optional.of(user));

                // when
                servlet.service(request, response);
            }

            // then
            assertThat(OBJECT_MAPPER.readTree(body.toString())).isEqualTo(expected);
        }

        @Test
        @DisplayName("JSON Content-Type으로 응답한다")
        void respondsWithJsonContentType() throws Exception {
            // given
            final var user = new User(1, "gugu", "password", "gugu@example.com");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(Optional.of(user));

                // when
                servlet.service(request, response);
            }

            // then
            verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        }

        @Test
        @DisplayName("응답에 비밀번호 값을 노출하지 않는다")
        void doesNotExposePasswordValue() throws Exception {
            // given
            final String password = "private-api-password";
            final var user = new User(1, "gugu", password, "gugu@example.com");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("gugu"))
                        .thenReturn(Optional.of(user));

                // when
                servlet.service(request, response);
            }

            // then
            assertThat(body.toString()).doesNotContain(password);
        }
    }

    @Nested
    @DisplayName("지원하지 않는 HTTP 메서드")
    class UnsupportedMethod {

        @DisplayName("GET 이외의 요청에는 사용자 조회 핸들러가 매핑되지 않는다")
        @ParameterizedTest(name = "[{index}] {0} /api/user → 매핑 없음")
        @ValueSource(strings = {"POST", "PUT", "DELETE"})
        void rejectsUnsupportedMethod(final String method) throws Exception {
            // given
            when(request.getMethod()).thenReturn(method);

            // when
            servlet.service(request, response);

            // then
            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
