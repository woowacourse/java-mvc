package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView view;
    private Map<String, Object> model;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws IOException {
        view = new JsonView();
        model = new HashMap<>();
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(resp.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    void singleValueJasonView() throws IOException {
        User tester = new User("tester");
        model.put("user", tester);

        view.render(model, req, resp);
        printWriter.flush();

        assertThat(stringWriter.toString()).isEqualTo(tester.toString());
    }

    @Test
    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.")
    void multiValueJasonView() throws IOException {
        User tester = new User("tester");
        User tester2 = new User("tester");
        model.put("user", tester);
        model.put("user2", tester2);

        view.render(model, req, resp);
        printWriter.flush();

        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(model);
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

    static class User {
        public User(String account) {
            this.account = account;
        }

        private final String account;

        public String getAccount() {
            return account;
        }

        @Override
        public String toString() {
            return getAccount();
        }
    }
}
