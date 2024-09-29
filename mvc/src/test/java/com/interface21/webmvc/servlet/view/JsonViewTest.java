package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class JsonViewTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter mockPrintWriter;
    private JsonView jsonView;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        mockPrintWriter = mock(PrintWriter.class);
        jsonView = new JsonView();
    }

    static class Dummy {
        private final String value;

        public Dummy(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @DisplayName("데이터가 하나일 때는 값을 그대로 반환한다")
    @Test
    void renderOnlyData_When_ModelHasOnlyOneData() throws Exception {
        Dummy dummy = new Dummy("test");
        Map<String, ?> model = Map.of("testModel", dummy);
        String expected = mapper.writeValueAsString(dummy);

        when(response.getWriter()).thenReturn(mockPrintWriter);
        doNothing().when(mockPrintWriter).write(argumentCaptor.capture());

        jsonView.render(model, request, response);

        assertThat(argumentCaptor.getValue()).isEqualTo(expected);
    }

    @DisplayName("데이터가 여러개일 때는 Map 형태로 반환한다")
    @Test
    void renderData_When_ModelHasMoreThanOneData() throws Exception {
        Dummy dummy1 = new Dummy("test1");
        Dummy dummy2 = new Dummy("test2");
        Map<String, Dummy> dummyData = Map.of("dummy1", dummy1, "dummy2", dummy2);
        String expected = mapper.writeValueAsString(dummyData);

        when(response.getWriter()).thenReturn(mockPrintWriter);
        doNothing().when(mockPrintWriter).write(argumentCaptor.capture());

        jsonView.render(dummyData, request, response);

        assertThat(argumentCaptor.getValue()).isEqualTo(expected);
    }
}
