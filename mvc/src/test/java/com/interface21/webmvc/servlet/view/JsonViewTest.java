package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void Writer를_얻기_전에_UTF_8_JSON_ContentType을_설정한다() throws Exception {
        view.render(Map.of("message", "안녕하세요"), request, response);

        final var responseOrder = inOrder(response);
        responseOrder.verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        responseOrder.verify(response).getWriter();
        assertThat(body.toString()).isEqualTo("\"안녕하세요\"");
    }

    @Test
    void 모델이_하나면_키로_감싸지_않고_객체를_직렬화한다() throws Exception {
        view.render(Map.of("user", new TestUser("gugu")), request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"gugu\"}"));
    }

    @Test
    void 모델이_둘_이상이면_모델의_키와_값을_그대로_직렬화한다() throws Exception {
        final Map<String, Object> model = Map.of("user", new TestUser("gugu"), "count", 1);

        view.render(model, request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"user\":{\"account\":\"gugu\"},\"count\":1}"));
    }

    @Test
    void 빈_모델은_빈_JSON_객체로_응답한다() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    @Test
    void 단일_모델의_값이_null이면_JSON_null로_응답한다() throws Exception {
        view.render(Collections.singletonMap("user", null), request, response);

        assertThat(body.toString()).isEqualTo("null");
    }

    @Test
    void 단일_모델의_값이_목록이면_JSON_배열로_응답한다() throws Exception {
        view.render(Map.of("accounts", List.of("gugu", "pobi")), request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("[\"gugu\",\"pobi\"]"));
    }

    public static class TestUser {

        private final String account;

        public TestUser(final String account) {
            this.account = account;
        }

        public String getAccount() {
            return account;
        }
    }
}
