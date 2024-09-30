package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class JsonViewStudyTest {

    @Test
    @DisplayName("model 의 값을 JSON 으로 변환해 response 를 반환한다.")
    void StingObjects() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        User user = new User("name", "password");
        Map<String, Object> model = Map.of(
                "user", user
        );

        var jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString())
                .isEqualTo(new ObjectMapper().writeValueAsString(user));
    }

    @Test
    @DisplayName("model 값이 여러개면 Map 형식을 변환한다.")
    void multipleObjects() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        Map<String, Object> model = Map.of(
                "user", new User("name", "password"),
                "someModel", new SomeModel("attribute")
        );

        var jsonView = new JsonView();
        jsonView.render(model, request, response);

        assertThat(stringWriter.toString())
                .isEqualTo(new ObjectMapper().writeValueAsString(model));
    }

    private static class User {
        private final String name;
        private final String password;

        private User(String name, String password) {
            this.name = name;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }
    }

    private static class SomeModel {
        private final String attribute;

        private SomeModel(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute() {
            return attribute;
        }
    }
}
