package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        jsonView = new JsonView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        objectMapper = new ObjectMapper();
    }

    @DisplayName("model에 값이 한 개이면, 값 자체를 json으로 반환한다.")
    @Test
    void render() throws Exception {
        // given
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        TestUser user = new TestUser("anna", 1L, "an@email.com");

        // when
        jsonView.render(Map.of("user", user), request, response);

        // then
        String expected = """
                {"id":1,"account":"anna","email":"an@email.com"}""";
        verify(writer).write(expected);
    }

    @DisplayName("model에 값이 두 개 이상이면, Map 자체를 json으로 반환한다.")
    @Test
    void renderMap() throws Exception {
        // given
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        TestUser user1 = new TestUser("anna", 1L, "an@email.com");
        TestUser user2 = new TestUser("kargo", 2L, "ka@email.com");
        Map<String, TestUser> model = Map.of("user1", user1, "user2", user2);

        // when
        jsonView.render(model, request, response);

        // then
        String expected = objectMapper.writeValueAsString(model);
        verify(writer).write(expected);
    }

    static class TestUser {

        private final long id;
        private final String account;
        private final String email;

        public TestUser(String account, long id, String email) {
            this.account = account;
            this.id = id;
            this.email = email;
        }

        @JsonGetter("id")
        public long getId() {
            return id;
        }

        @JsonGetter("account")
        public String getAccount() {
            return account;
        }

        @JsonGetter("email")
        public String getEmail() {
            return email;
        }
    }
}
