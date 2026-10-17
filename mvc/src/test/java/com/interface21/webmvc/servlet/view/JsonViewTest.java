package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonViewTest {
    JsonView jsonView;
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        jsonView = new JsonView();
        objectMapper = new ObjectMapper();
    }

    @Test
    void 모델이_하나면_값만_JSON으로_반환한다() throws Exception {
        Map<String, ?> model = Map.of("user", Map.of("account", "gugu"));

        String body = render(model);

        assertThat(objectMapper.readTree(body))
                .isEqualTo(objectMapper.readTree("""
                {"account":"gugu"}
                """));
    }

    @Test
    void 모델이_여러개면_Map_전체를_JSON으로_반환한다() throws Exception {
        Map<String, ?> model = Map.of(
                "account", "gugu",
                "name", "구구"
        );

        String body = render(model);

        assertThat(objectMapper.readTree(body))
                .isEqualTo(objectMapper.readTree("""
                {"account":"gugu","name":"구구"}
                """));
    }

    private String render(Map<String, ?> model) throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter output = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(output));

        jsonView.render(model, request, response);

        return output.toString();
    }
}
