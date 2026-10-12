package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    @Test
    @DisplayName("모델 값이 하나면 값 자체를 JSON으로 응답하고 UTF-8 콘텐츠 타입을 설정한다")
    void rendersSingleModelValue() throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("user", Map.of("account", "초록")),
                mock(HttpServletRequest.class), response);

        final var inOrder = inOrder(response);
        inOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        inOrder.verify(response).getWriter();
        assertThat(body.toString()).isEqualTo("{\"account\":\"초록\"}");
    }

    @Test
    @DisplayName("모델 값이 둘 이상이면 키를 포함한 Map 전체를 JSON으로 응답한다")
    void rendersMultipleModelValues() throws Exception {
        final var response = mock(HttpServletResponse.class);
        final var body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        new JsonView().render(Map.of("user", Map.of("account", "gugu"), "count", 2),
                mock(HttpServletRequest.class), response);

        final var mapper = new ObjectMapper();
        assertThat(mapper.readTree(body.toString()))
                .isEqualTo(mapper.readTree("{\"user\":{\"account\":\"gugu\"},\"count\":2}"));
    }
}
