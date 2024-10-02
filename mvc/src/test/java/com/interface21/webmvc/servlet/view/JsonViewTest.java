package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JsonViewTest {

    @DisplayName("Map에 저장된 데이터를 json으로 직렬화 성공")
    @Test
    void serialize() throws Exception {
        //given
        View jsonView = new JsonView();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, Object> model = new HashMap<>();
        model.put("name", "capy");
        model.put("age", 26);

        //when
        jsonView.render(model, request, response);
        printWriter.flush();
        String jsonOutput = stringWriter.toString();

        //then
        String expectedJson = "{\"name\":\"capy\",\"age\":26}";
        assertThat(jsonOutput).isEqualTo(expectedJson);
    }

    @DisplayName("직렬화 시 response의 contentType을 APPLICATION_JSON_UTF8_VALUE으로 설정 성공")
    @Test
    void serialize_contentType() throws Exception {
        //given
        View jsonView = new JsonView();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        Map<String, Object> model = new HashMap<>();

        //when
        jsonView.render(model, request, response);

        //then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
