package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class JsonViewTest {

    private final JsonView view = new JsonView();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final StringWriter body = new StringWriter();

    @BeforeEach
    void setUp() throws Exception {
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    @DisplayName("모델에 항목이 하나면 속성 이름을 제외하고 객체 자체를 JSON으로 응답한다")
    void rendersSingleValueWithoutAttributeName() throws Exception {
        Map<String, Object> model = Map.of("user", new TestUser("gugu", "구구"));

        view.render(model, request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("""
                        {"account":"gugu","name":"구구"}
                        """));
    }

    @Test
    @DisplayName("모델에 항목이 여러 개면 속성 이름을 포함한 Map 전체를 JSON으로 응답한다")
    void rendersMultipleValuesWithAttributeNames() throws Exception {
        Map<String, Object> model = Map.of(
                "user", new TestUser("gugu", "구구"),
                "count", 1
        );

        view.render(model, request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("""
                        {"user":{"account":"gugu","name":"구구"},"count":1}
                        """));
    }

    @Test
    @DisplayName("모델의 유일한 값이 리스트이면 리스트 전체를 JSON 배열로 응답한다")
    void rendersSingleListValueAsArray() throws Exception {
        Map<String, Object> model = Map.of("accounts", List.of("gugu", "jerry"));

        view.render(model, request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("""
                        ["gugu","jerry"]
                        """));
    }

    @Test
    @DisplayName("Writer를 얻기 전에 JSON UTF-8 Content-Type을 설정하고 한글을 응답한다")
    void setsJsonUtf8ContentTypeBeforeObtainingWriter() throws Exception {
        view.render(Map.of("message", "안녕하세요"), request, response);

        InOrder order = inOrder(response);
        order.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        order.verify(response).getWriter();
        assertThat(body.toString()).isEqualTo("\"안녕하세요\"");
    }

    public record TestUser(String account, String name) {
    }
}
