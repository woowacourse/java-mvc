package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class JsonViewTest {

    @Test
    void renderSingleModelTest() throws Exception {
        StringWriter stringWriter = new StringWriter();
        final var jsonView = new JsonView();
        final var model = Map.of("testObject", new TestObject("test", 1));
        final var request = new MockHttpServletRequest();

        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        jsonView.render(model, request, response);

        String expected = """
                {"stringValue":"test","intValue":1}""";

        assertThat(stringWriter.toString()).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void renderMultiModelTest() throws Exception {
        StringWriter stringWriter = new StringWriter();
        final var jsonView = new JsonView();
        final Map<String, Object> model = new LinkedHashMap<>();
        model.put("testObject", new TestObject("test", 1));
        model.put("testObject2", new TestObject("test2", 2));
        final var request = new MockHttpServletRequest();

        final var response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        jsonView.render(model, request, response);

        String expected = """
                {"testObject":{"stringValue":"test","intValue":1},"testObject2":{"stringValue":"test2","intValue":2}}""";

        assertThat(stringWriter.toString()).isEqualTo(expected);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    static class TestObject {

        private final String stringValue;
        private final int intValue;

        public TestObject(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }
    }
}
