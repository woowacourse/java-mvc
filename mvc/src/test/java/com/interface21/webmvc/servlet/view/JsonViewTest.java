package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void 데이터가_한_개면_값을_그대로_Json으로_변환해서_반환() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> model = new HashMap<>();
        model.put("user", "ted");

        JsonView jsonView = new JsonView();

        // when
        jsonView.render(model, request, response);

        // then
        printWriter.flush();
        String actual = stringWriter.toString();
        String expected = "\"ted\"";
        assertThat(actual).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void 데이터가_두_개_이상이면_Map_형태_그대로_Json으로_변환해서_반환() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, String> model = new HashMap<>();
        model.put("user", "ted");
        model.put("email", "ted1@example.com");

        JsonView jsonView = new JsonView();

        // when
        jsonView.render(model, request, response);

        // then
        printWriter.flush();
        String actual = stringWriter.toString();
        String expected = "{\"user\":\"ted\",\"email\":\"ted1@example.com\"}";
        assertThat(actual).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
