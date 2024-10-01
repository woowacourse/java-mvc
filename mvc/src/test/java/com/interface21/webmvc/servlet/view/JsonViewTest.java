package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;

class JsonViewTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource
    @DisplayName("Render a model to json.")
    void render(Map<String, Object> model, String expected) throws Exception {
        // given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        var sut = new JsonView();

        // when
        sut.render(model, request, response);

        // then
        verify(printWriter).write(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    static Stream<Arguments> render() throws JsonProcessingException {
        var user = new User("Arachneee", "1q2w3e4r!");
        var model1 = Map.of("account", user.account);
        var model2 = Map.of("user", user);
        return Stream.of(
                Arguments.of(model1, objectMapper.writeValueAsString(user.account)),
                Arguments.of(model2, objectMapper.writeValueAsString(user))
        );
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("Render multiple model to json.")
    void renderMultiple(Map<String, Object> model, String expected) throws Exception {
        // given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);
        var printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        var sut = new JsonView();

        // when
        sut.render(model, request, response);

        // then
        verify(printWriter).write(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    static Stream<Arguments> renderMultiple() throws JsonProcessingException {
        var user1 = new User("Arachneee", "1q2w3e4r!");
        var user2 = new User("gugu", "password");
        var user3 = new User("zeus", "secret");
        var model1 = Map.of("user1", user1, "user2", user2);
        var model2 = Map.of("user1", user1, "user2", user2, "user3", user3);
        return Stream.of(
                Arguments.of(model1, objectMapper.writeValueAsString(model1)),
                Arguments.of(model2, objectMapper.writeValueAsString(model2))
        );
    }

    record User(String account, String password) {
    }
}
