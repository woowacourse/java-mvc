package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("Json으로 모델을 변환한다.")
    void convertModelTest() throws IOException {
        JsonView view = new JsonView();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out, true);
        when(response.getWriter()).thenReturn(writer);
        view.render(Map.of("account", "테니"), request, response);

        assertThat(out.toString(StandardCharsets.UTF_8)).isEqualTo("{\"account\":\"테니\"}");
    }
}
