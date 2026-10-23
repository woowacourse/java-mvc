package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter output;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        output = new StringWriter();
        writer = spy(new PrintWriter(output));
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    @DisplayName("모델이 비어 있으면 빈 JSON 객체를 응답한다")
    void render_empty_model() throws Exception {
        new JsonView().render(Map.of(), request, response);

        assertThat(objectMapper.readTree(output.toString()).isObject()).isTrue();
        assertThat(objectMapper.readTree(output.toString()).isEmpty()).isTrue();
    }

    @Test
    @DisplayName("모델이 하나면 이름을 제외한 객체 값만 JSON으로 응답한다")
    void render_single_object_value() throws Exception {
        final var user = new TestUser("구구");
        final var model = Map.of("user", user);

        new JsonView().render(model, request, response);

        final var json = objectMapper.readTree(output.toString());
        assertThat(json.size()).isEqualTo(1);
        assertThat(json.get("name").asText()).isEqualTo("구구");
        assertThat(model).containsOnlyKeys("user");
        assertThat(model.get("user")).isSameAs(user);
    }

    @Test
    @DisplayName("모델이 여러 개면 이름과 값을 포함한 JSON 객체를 응답한다")
    void render_multiple_model_entries() throws Exception {
        final var model = Map.of("name", "구구", "age", 20);

        new JsonView().render(model, request, response);

        assertThat(objectMapper.readTree(output.toString())).isEqualTo(objectMapper.valueToTree(model));
    }

    @Test
    @DisplayName("모델 하나의 값이 리스트이면 JSON 배열로 응답한다")
    void render_single_list_value() throws Exception {
        new JsonView().render(Map.of("users", List.of(new TestUser("구구"), new TestUser("루드"))), request, response);

        final var json = objectMapper.readTree(output.toString());
        assertThat(json.isArray()).isTrue();
        assertThat(json.size()).isEqualTo(2);
        assertThat(json.get(0).get("name").asText()).isEqualTo("구구");
        assertThat(json.get(1).get("name").asText()).isEqualTo("루드");
    }

    @Test
    @DisplayName("모델 하나의 값이 null이면 JSON null로 응답한다")
    void render_single_null_value() throws Exception {
        new JsonView().render(Collections.singletonMap("user", null), request, response);

        assertThat(objectMapper.readTree(output.toString()).isNull()).isTrue();
    }

    @Test
    @DisplayName("writer를 얻기 전에 JSON UTF-8 응답 타입을 지정하고 writer를 닫지 않는다")
    void set_content_type_before_writer_and_leave_writer_open() throws Exception {
        new JsonView().render(Map.of("name", "구구"), request, response);

        final var order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        verify(writer, never()).close();
        assertThat(objectMapper.readTree(output.toString()).asText()).isEqualTo("구구");
    }

    @Test
    @DisplayName("응답 writer를 얻지 못하면 원인 예외를 그대로 전달한다")
    void propagate_writer_failure() throws Exception {
        final var failure = new IOException("writer failed");
        when(response.getWriter()).thenThrow(failure);

        assertThatThrownBy(() -> new JsonView().render(Map.of("name", "구구"), request, response))
                .isSameAs(failure);
    }

    public static class TestUser {

        private final String name;

        public TestUser(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
