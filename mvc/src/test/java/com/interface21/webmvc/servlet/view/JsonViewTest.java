package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseBody;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseBody = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
    }

    @Test
    void 모델에_데이터가_한_개일_경우_값만_JSON으로_응답한다() throws Exception {
        JsonView jsonView = new JsonView();
        Map<String, ?> model = Map.of("message", "테스트");

        jsonView.render(model, request, response);

        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(responseBody.toString()).isEqualTo("\"테스트\"");
    }

    @Test
    void 모델에_데이터가_여러_개면_Map_전체를_JSON으로_응답한다() throws Exception {
        JsonView jsonView = new JsonView();
        Map<String, ?> model = Map.of("message", "hello", "count", 1);

        jsonView.render(model, request, response);

        Map<?, ?> actual = objectMapper.readValue(responseBody.toString(), Map.class);
        assertThat(actual).isEqualTo(Map.of("message", "hello", "count", 1));
    }

    @Test
    void 모델이_비어있으면_빈_JSON_객체로_응답한다() throws Exception {
        JsonView jsonView = new JsonView();

        jsonView.render(Map.of(), request, response);

        assertThat(responseBody.toString()).isEqualTo("{}");
    }
}
