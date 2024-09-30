package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class JsonViewTest {

    private JsonView view;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        view = new JsonView();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        Mockito.when(response.getWriter())
                .thenReturn(writer);
    }

    @Test
    @DisplayName("모델이 없을 때는 빈 JSON을 출력한다.")
    void some() throws Exception {
        view.render(Map.of(), request, response);

        assertThat(stringWriter.toString()).isBlank();
    }

    @Test
    @DisplayName("모델이 하나일 때는 모델의 값을 바로 출력한다.")
    void some2() throws Exception {
        final Map<String, Object> model = Map.of("name", "potato");
        view.render(model, request, response);
        assertThat(stringWriter.toString()).hasToString("potato");
    }

    @Test
    @DisplayName("모델이 여러개일 때는 모델을 JSON으로 변환하여 출력한다.")
    void some1() throws Exception {
        final Map<String, Object> model = Map.of("name", "potato", "age", 10);
        view.render(model, request, response);
        assertAll(()->{
            assertThat(stringWriter.toString()).hasToString("name:");
            assertThat(stringWriter.toString()).hasToString("age:");
        });
    }
}
