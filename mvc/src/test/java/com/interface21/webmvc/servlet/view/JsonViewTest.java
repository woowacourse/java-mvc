package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class JsonViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    @DisplayName("응답을 Json 형식으로 반환 성공: 데이터가 1개면 그대로 반환")
    void renderOneData() throws Exception {
        // given
        Map<String, ?> model = Map.of("alpaca", new User("alpaca", 25));

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(writer).write("{\"name\":\"alpaca\",\"age\":25}");
    }

    @Test
    @DisplayName("응답을 Json 형식으로 반환 성공: 데이터가 2개 이상이면 Map 형태의 Json으로 반환")
    void renderOverTwoData() throws Exception {
        // given
        Map<String, ?> model = Map.of(
                "alpaca", new User("alpaca", 25),
                "moly", new User("moly", 20)
        );
        String expectedJson = new ObjectMapper().writeValueAsString(model);

        // when
        JsonView jsonView = new JsonView();
        jsonView.render(model, request, response);

        // then
        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(writer).write(expectedJson);
        assertThat(expectedJson).contains(
                "alpaca\":{\"name\":\"alpaca\",\"age\":25}",
                "moly\":{\"name\":\"moly\",\"age\":20}"
        );
    }

    private record User(String name, int age) {
    }
}
