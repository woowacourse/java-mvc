package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    @DisplayName("Serializable 한 값을 JSON 으로 렌더링 한다.")
    void renderWithPrimitive() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Skill skill = new Skill(List.of("Korean", "English"), List.of("Javascript", "Java"));
        Map<String, ?> model = Map.of("user", "seyang", "langs", skill);
        JsonView jsonView = new JsonView();
        OutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // when
        when(response.getWriter()).thenReturn(writer);
        jsonView.render(model, request, response);
        writer.flush();

        // then
        verify(response).setContentType("application/json;charset=UTF-8");
        assertThat(outputStream.toString()).satisfiesAnyOf(
                s -> assertThat(s).isEqualTo(
                        "{\"langs\":{\"langs\":[\"Korean\",\"English\"],\"codes\":[\"Javascript\",\"Java\"]},\"user\":\"seyang\"}"),
                s -> assertThat(s).isEqualTo(
                        "{\"user\":\"seyang\",\"langs\":{\"langs\":[\"Korean\",\"English\"],\"codes\":[\"Javascript\",\"Java\"]}}")
        );
    }

    record Skill(List<String> langs, List<String> codes) {
    }
}
