package com.interface21.webmvc.servlet.view;

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
    private final JsonView jsonView = new JsonView();

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
    void ContentType을_JSON으로_설정한다() throws Exception {
        jsonView.render(Map.of("id", "gugu"), request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void model에_데이터가_1개면_값을_그대로_JSON으로_반환한다() throws Exception {
        jsonView.render(Map.of("user", new SampleUser("gugu", "gugu@email.com")), request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"account\":\"gugu\",\"email\":\"gugu@email.com\"}"));
    }

    @Test
    void model에_데이터가_2개_이상이면_Map_형태로_JSON을_반환한다() throws Exception {
        jsonView.render(Map.of("id", "gugu", "email", "gugu@email.com"), request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree("{\"id\":\"gugu\",\"email\":\"gugu@email.com\"}"));
    }

    public record SampleUser(String account, String email) {
    }
}
