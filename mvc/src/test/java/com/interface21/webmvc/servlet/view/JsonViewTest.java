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
import java.util.Map;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    @Test
    void JsonView_content_type을_설정한다() throws Exception{
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View view = new JsonView();

        // when
        view.render(Collections.emptyMap(), request, response);

        // then
        verify(response).setContentType("application/json;charset=UTF-8");
    }

    @Test
    void JsonView_데이터가_한_개인() throws Exception{
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View view = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("userA", new TestObject("userA", 10));

        // when
        view.render(model, request, response);

        // then
        verify(writer).write("{\"name\":\"userA\",\"age\":10}");
    }

    @Test
    void JsonView_데이터가_여러개인() throws Exception{
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View view = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("userA", new TestObject("userA", 10));
        model.put("userB", new TestObject("userB", 20));

        // when
        view.render(model, request, response);

        // then
        verify(writer).write("{\"userA\":{\"name\":\"userA\",\"age\":10},\"userB\":{\"name\":\"userB\",\"age\":20}}");
    }
}
