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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private JsonView view;

    @BeforeEach
    void setUp() {
        view = JsonView.from();
    }

    @DisplayName("생성된 인스턴스는 항상 같은 인스턴스이다.")
    @Test
    void should_getSameInstance_when_construct() {
        // when
        JsonView created = JsonView.from();

        // then
        assertThat(created).isSameAs(view);
    }

    @DisplayName("model에 데이터가 1개면 값을 그대로 반환한다.")
    @Test
    void should_returnNaiveData_when_givenModelOne() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        Map<String, Object> model = new HashMap<>();
        model.put("key", "value");

        // when
        view.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.toString()).isEqualTo("value");
    }

    @DisplayName("model에 데이터가 2개 이상이면 Map 형태 그대로 JSON으로 변환해서 반환한다.")
    @Test
    void should_returnJsonData_when_givenModelMoreThanTwo() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        Map<String, Object> model = new HashMap<>();
        model.put("key1", "value1");
        model.put("key2", "value2");

        // when
        view.render(model, request, response);

        // then
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.toString()).isEqualTo("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }
}
