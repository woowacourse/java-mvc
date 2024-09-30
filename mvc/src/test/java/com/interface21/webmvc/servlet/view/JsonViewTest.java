package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void JSON_content_type을_설정한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View jsonView = new JsonView();

        // when
        jsonView.render(Collections.emptyMap(), request, response);

        // then
        verify(response).setContentType("application/json;charset=UTF-8");
    }

    @Test
    void model에_데이터가_한_개이면_값을_그대로_반환한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View jsonView = new JsonView();

        // when
        Map<String, Object> model = Collections.singletonMap("user", new User("prin", "backend"));
        jsonView.render(model, request, response);

        // then
        String result = """
                {"name":"prin","group":"backend"}""";
        verify(writer).write(result);
    }

    @Test
    void model에_데이터가_2개_이상이면_전체를_JSON으로_변환한다() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View jsonView = new JsonView();

        // when
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("user1", new User("prin", "backend"));
        model.put("user2", new User("maru", "frontend"));

        jsonView.render(model, request, response);

        // then
        String result = """
                {"user1":{"name":"prin","group":"backend"},"user2":{"name":"maru","group":"frontend"}}""";
        verify(writer).write(result);
    }

    private record User(String name, String group) {
    }
}
