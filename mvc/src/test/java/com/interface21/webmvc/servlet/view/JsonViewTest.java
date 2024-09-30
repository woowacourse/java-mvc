package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @DisplayName("인자로 받은 model을 json 형태로 직렬화해서 response의 body로 응답한다.")
    @Test
    void getJsonOfModelData() throws Exception {
        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("user1", new TestUser("LeeSang", 25));
        model.put("user2", new TestUser("HaeSsee", 7));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);
        printWriter.flush();

        String expectedJson = "{\"user1\":{\"name\":\"LeeSang\",\"age\":25},\"user2\":{\"name\":\"HaeSsee\",\"age\":7}}";
        assertThat(stringWriter.toString())
                .isEqualTo(expectedJson);
    }

    @DisplayName("인자로 받은 model의 데이터가 1개면 값을 그대로 response의 body로 응답한다.")
    @Test
    void getJsonOfModelDataWhenOnlyOne() throws Exception {
        JsonView jsonView = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("user", new TestUser("LeeSangHaeSsee", 1));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        jsonView.render(model, request, response);
        printWriter.flush();

        String expectedJson = "{\"name\":\"LeeSangHaeSsee\",\"age\":1}";
        assertThat(stringWriter.toString())
                .isEqualTo(expectedJson);
    }

    record TestUser(String name, int age) {

    }
}
