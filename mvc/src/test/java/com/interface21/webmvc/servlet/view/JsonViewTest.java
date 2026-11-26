package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.DelegatingServletOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final ByteArrayOutputStream body = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(body, true, StandardCharsets.UTF_8);

    @BeforeEach
    void setUp() throws Exception {
        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(body));
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    @DisplayName("모델 데이터가 1개면 키를 제외한 값만 JSON으로 응답한다")
    void renderSingleValue() throws Exception {
        final var model = Map.of("user", new User("예지", 20));

        view.render(model, request, response);

        writer.flush();
        final var actual = objectMapper.readTree(body.toByteArray());
        final var expected = objectMapper.readTree("""
                {"name": "예지", "age": 20}
                """);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("모델 데이터가 2개 이상이면 모델 Map 전체를 JSON으로 응답한다")
    void renderMultipleValues() throws Exception {
        final var model = Map.of("user", new User("예지", 20), "count", 1);

        view.render(model, request, response);

        writer.flush();
        final var actual = objectMapper.readTree(body.toByteArray());
        final var expected = objectMapper.readTree("""
                {"user": {"name": "예지", "age": 20}, "count": 1}
                """);
        assertThat(actual).isEqualTo(expected);
    }

    private record User(String name, int age) {
    }
}
