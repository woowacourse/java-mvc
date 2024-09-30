package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private PrintWriter writer;
    private StringWriter stringWriter;
    private Map<String, String> model;

    @BeforeEach
    void name() {
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        model = new HashMap<>();
    }

    @Test
    @DisplayName("model이 하나인 경우 그대로 반환한다.")
    void render_WhenSingleModel() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        JsonView jsonView = new JsonView();

        when(response.getWriter()).thenReturn(writer);

        String expected = """
                User{id=1, account='gugu', email='hkkang@woowahan.com', password='password'} """;
        model.put("name", expected);

        jsonView.render(model, request, response);
        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("model이 여러개인 경우 Json으로 만들어 반환한다.")
    void render() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        JsonView jsonView = new JsonView();

        when(response.getWriter()).thenReturn(writer);

        String expectedName = "polla";
        String expectedComment = "집에 가고 싶어요.";
        model.put("name", expectedName);
        model.put("comment", expectedComment);

        String expectedResult = """
                {"name":"polla","comment":"집에 가고 싶어요."} """;

        jsonView.render(model, request, response);
        assertThat(stringWriter.toString()).isEqualTo(expectedResult);
    }
}
