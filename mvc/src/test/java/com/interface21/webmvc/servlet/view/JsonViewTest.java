package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JsonViewTest {

    private JsonView jsonView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() throws Exception {
        jsonView = new JsonView();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }


    @DisplayName("모델에 데이터가 한 개인 경우 그대로 반환한다.")
    @Test
    void renderSingleModel() throws Exception {
        Map<String, String> model = new HashMap<>();
        model.put("key", "value");

        jsonView.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo("\"value\"");
    }

    @DisplayName("모델에 객체 데이터가 있는 경우 그대로 반환한다.")
    @Test
    void renderSingleObjectModel() throws Exception {

        class TestObject {
            private final String name;
            private final Long number;

            public TestObject(String name, Long number) {
                this.name = name;
                this.number = number;
            }

            public String getName() {
                return name;
            }

            public Long getNumber() {
                return number;
            }
        }

        Map<String, TestObject> model = Map.of("test", new TestObject("testName", 1L));

        jsonView.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo("{\"name\":\"testName\",\"number\":1}");
    }

    @DisplayName("모델에 데이터가 두 개 이상이면 Map 형태 그대로 반환한다.")
    @Test
    void renderMultipleModels() throws Exception {
        Map<String, String> model = new LinkedHashMap<>();
        model.put("key1", "value1");
        model.put("key2", "value2");

        jsonView.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }

    @DisplayName("모델에 데이터가 없는 경우 비어 있는 Map을 반환한다.")
    @Test
    void renderEmptyModel() throws Exception {
        Map<String, Object> model = new HashMap<>();

        jsonView.render(model, request, response);

        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(stringWriter.toString()).isEqualTo("{}");
    }
}
