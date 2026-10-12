package com.interface21.webmvc.servlet.view;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter body;

    @BeforeEach
    void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));
    }

    @Test
    void JSON_컨텐츠_타입으로_응답한다() throws Exception {
        new JsonView().render(Map.of("user", new TestUser("gugu", "gugu@email.com")), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 모델에_데이터가_1개면_값을_그대로_JSON으로_변환한다() throws Exception {
        new JsonView().render(Map.of("user", new TestUser("gugu", "gugu@email.com")), request, response);

        assertThat(readBody()).isEqualTo(Map.of("account", "gugu", "email", "gugu@email.com"));
    }

    @Test
    void 모델에_데이터가_2개_이상이면_Map_형태로_JSON으로_변환한다() throws Exception {
        new JsonView().render(Map.of("account", "gugu", "email", "gugu@email.com"), request, response);

        assertThat(readBody()).isEqualTo(Map.of("account", "gugu", "email", "gugu@email.com"));
    }

    @Test
    void 모델이_비어있으면_빈_JSON_객체로_변환한다() throws Exception {
        new JsonView().render(Map.of(), request, response);

        assertThat(body.toString()).isEqualTo("{}");
    }

    private Map<String, Object> readBody() throws Exception {
        return objectMapper.readValue(body.toString(), new TypeReference<>() {
        });
    }

    record TestUser(String account, String email) {
    }
}
