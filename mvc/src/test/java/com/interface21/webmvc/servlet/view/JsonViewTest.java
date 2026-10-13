package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JsonViewTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void singleModel() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        final Map<String, Object> model =
                Map.of("user", new UserData("gugu"));

        new JsonView().render(model, request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree(
                        """
                                {"account":"gugu"}
                                """));
        verify(response).setContentType(
                MediaType.APPLICATION_JSON_UTF8_VALUE
        );
    }

    @Test
    void multipleModels() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        final Map<String, Object> model = Map.of(
                "user", new UserData("gugu"),
                "count", 1
        );

        new JsonView().render(model, request, response);

        assertThat(objectMapper.readTree(body.toString()))
                .isEqualTo(objectMapper.readTree(
                        """
                                {"user":{"account":"gugu"},"count":1}
                                """));
    }

    public record UserData(String account) {
    }
}
