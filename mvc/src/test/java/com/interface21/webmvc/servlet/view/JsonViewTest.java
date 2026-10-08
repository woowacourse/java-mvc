package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JsonViewTest {

    @DisplayName("모델에 객체가 1개라면 해당 객체만 직렬화한다.")
    @Test
    void serializationSingleModelValue() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var model = Map.of("user", new User(1L, "tory", "password", "woowa@techcourse.com"));

        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        // when
        new JsonView().render(model, request, response);
        printWriter.flush();

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).getWriter();
        assertThat(stringWriter.toString())
                .isEqualTo("{\"account\":\"tory\"}");
    }

    @DisplayName("모델에 객체가 2개이상 이라면 Map 형태로 직렬화한다.")
    @Test
    void serializationMultiModelValue() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final var model = new LinkedHashMap<String, Object>();
        model.put("user", new User(1L, "tory", "password", "woowa@techcourse.com"));
        model.put("user2", new User(2L, "tory2", "password", "woowa@techcourse.com"));

        final var stringWriter = new StringWriter();
        final var printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        new JsonView().render(model, request, response);

        printWriter.flush();

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        verify(response).getWriter();

        assertThat(stringWriter.toString())
                .isEqualTo("{\"user\":{\"account\":\"tory\"},\"user2\":{\"account\":\"tory2\"}}");
    }
}
