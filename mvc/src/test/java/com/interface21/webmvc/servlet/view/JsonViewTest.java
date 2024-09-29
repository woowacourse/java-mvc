package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private ObjectMapper objectMapper;
    private Map<String, User> model;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        model = new HashMap<>();
    }

    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void givenSingleValue_thenReturnValue() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        User user1 = addUserToModel("user1", 1);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        String expectedJson = objectMapper.writeValueAsString(user1);

        verify(writer).write(expectedJson);
    }

    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 반환한다.")
    @Test
    void givenMultiValue_thenReturnModel() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        addUserToModel("user1", 1);
        addUserToModel("user2", 2);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        String expectedJson = objectMapper.writeValueAsString(model);

        verify(writer).write(expectedJson);
    }

    @DisplayName("MediaType을 APPLICATION_JSON_UTF8_VALUE로 설정한다.")
    @Test
    void givenMode() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private User addUserToModel(String name, int age) {
        User user = new User(name, age);
        model.put(name, user);
        return user;
    }

    class User {

        @JsonProperty("name")
        private String name;

        @JsonProperty("age")
        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

}
