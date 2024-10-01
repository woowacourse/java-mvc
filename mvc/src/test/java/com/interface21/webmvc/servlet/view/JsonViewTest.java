package com.interface21.webmvc.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.View;

import samples.TestUser;


class JsonViewTest {

    @DisplayName("model에 데이터가 1개면 값을 반환한다.")
    @Test
    void returnValueOneDataInModel() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View jsonView = new JsonView();
        final TestUser testUser = new TestUser(1L, "abc", "password", "abc@gooogle.com");

        // when
        jsonView.render(Map.of("testUser", testUser), request, response);

        // then
        String result = """
                {"id":1,"account":"abc","password":"password","email":"abc@gooogle.com"}""";
        verify(writer).write(result);
    }

    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다")
    @Test
    void returnMapTwoDataInModel() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        View jsonView = new JsonView();

        // when
        Map<String, TestUser> model = new LinkedHashMap<>();
        model.put("testUser1", new TestUser(1L, "abc", "password", "abc@google.com"));
        model.put("testUser2", new TestUser(2L, "def", "password", "def@google.com"));

        jsonView.render(model, request, response);

        // then
        String result = """
                {"testUser1":{"id":1,"account":"abc","password":"password","email":"abc@google.com"},"testUser2":{"id":2,"account":"def","password":"password","email":"def@google.com"}}""";
        verify(writer).write(result);
    }
}
