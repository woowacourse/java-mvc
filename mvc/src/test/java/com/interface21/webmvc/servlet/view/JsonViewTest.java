package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("model에 단일 객체가 존재할 경우 해당 객체만 JSON으로 변환하여 반환한다.")
    @Test
    void renderWithSingleObject() throws Exception {
        // given
        final var view = new JsonView();
        final var model = Map.of("user", new User("gugu", "password", "hkkang@woowahan.com"));
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        view.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final var objectMapper = new ObjectMapper();
        final var user = objectMapper.readValue(stringWriter.toString(), User.class);
        assertThat(user.getAccount()).isEqualTo("gugu");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("hkkang@woowahan.com");
    }

    @DisplayName("model에 다수의 객체가 존재할 경우 map 전체를 JSON으로 변환하여 반환한다.")
    @Test
    void renderWithMultipleObjects() throws Exception {
        // given
        final var view = new JsonView();
        final var model = Map.of(
                "user", new User("gugu", "password", "hkkang@woowahan.com"),
                "message", "Hello"
        );
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        // when
        view.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final var objectMapper = new ObjectMapper();
        final Map<String, Object> json = objectMapper.readValue(stringWriter.toString(), new TypeReference<>() {});
        assertThat(json.get("message")).isEqualTo("Hello");
        final var userMap = (Map<String, String>) json.get("user");
        assertThat(userMap.get("account")).isEqualTo("gugu");
        assertThat(userMap.get("password")).isEqualTo("password");
        assertThat(userMap.get("email")).isEqualTo("hkkang@woowahan.com");
    }

    // 테스트용 User
    private static class User {
        private String account;
        private String password;
        private String email;

        public User() {
        }

        public User(String account, String password, String email) {
            this.account = account;
            this.password = password;
            this.email = email;
        }

        public String getAccount() {
            return account;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }
}
