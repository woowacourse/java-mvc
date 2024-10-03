package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private StringWriter stringWriter;
    private PrintWriter printWriter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() throws IOException {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(printWriter);
    }

    @DisplayName("model에 담긴 데이터가 하나일 경우 값을 그대로 직렬화 한다.")
    @Test
    void viewOneModel() throws Exception {
        // given
        String expected = "{\"name\":\"poke\"}";
        Map<String, Object> model = new HashMap<>();
        model.put("user", new TestUser("poke", 500));
        JsonView view = new JsonView();

        // when
        view.render(model, request, response);
        String actual = stringWriter.toString();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("model에 담긴 데이터가 두개일 경우 model을 그대로 직렬화 한다.")
    @Test
    void viewMoreThanOneModel() throws Exception {
        // given
        String expected = "{\"user1\":{\"name\":\"poke\"},\"user2\":{\"name\":\"TACAN\"}}";
        Map<String, Object> model = new HashMap<>();
        model.put("user1", new TestUser("poke", 500));
        model.put("user2", new TestUser("TACAN", 5000));
        JsonView view = new JsonView();

        // when
        view.render(model, request, response);
        String actual = stringWriter.toString();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    class TestUser {
        private String name;
        private int age;

        public TestUser(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }
    }
}