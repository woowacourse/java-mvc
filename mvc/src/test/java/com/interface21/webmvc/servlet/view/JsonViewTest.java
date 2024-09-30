package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockHttpServletRequest;

class JsonViewTest {

    @Test
    @DisplayName("model에 데이터가 1개인 경우 값을 그대로 반환한다.")
    void renderModelWithOneData() throws Exception {
        // given
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        final var sut = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);
        Map<String, Object> model = new HashMap<>();
        model.put("test1", new TestDomain("test1"));

        // when
        sut.render(model, request, response);

        // then
        verify(writer).write(captor.capture());
        assertThat(captor.getValue()).isEqualTo("{\"name\":\"test1\"}");
    }

    @Test
    @DisplayName("model에 데이터가 2개 이상인 경우 Map 형태로 값을 반환한다.")
    void renderModelWithTwoData() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        JsonView view = new JsonView();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);
        Map<String, Object> model = new HashMap<>();
        model.put("test1", new TestDomain("test1"));
        model.put("test2", new TestDomain("test2"));

        view.render(model, request, response);

        verify(writer).write(captor.capture());

        assertThat(captor.getValue()).contains("\"test2\":{\"name\":\"test2\"}", "\"test1\":{\"name\":\"test1\"}");
    }

    private class TestDomain {
        private String name;

        public TestDomain(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
