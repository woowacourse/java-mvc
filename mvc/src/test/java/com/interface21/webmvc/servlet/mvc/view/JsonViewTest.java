package com.interface21.webmvc.servlet.mvc.view;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class JsonViewTest {

    @Test
    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    void returnElementWhenModelSizeIsOne() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", new User("gooreum", 25));

        // when
        jsonView.render(model, request, response);

        // then
        assertAll(
                () -> verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE),
                () -> verify(response.getWriter()).write("{\"name\":\"gooreum\",\"age\":25}")
        );
    }

    @Test
    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 반환한다.")
    void returnMapWhenModelHasMoreThanOneElement() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        JsonView jsonView = new JsonView();
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("user1", new User("gooreum1", 1));
        model.put("user2", new User("gooreum2", 2));

        // when
        jsonView.render(model, request, response);

        // then
        assertAll(
                () -> verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE),
                () -> verify(response.getWriter()).write("{\"user1\":{\"name\":\"gooreum1\",\"age\":1},\"user2\":{\"name\":\"gooreum2\",\"age\":2}}")
        );
    }

    record User(String name, int age) {
    }
}
