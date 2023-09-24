package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class JsonViewTest {

    @Mock
    HttpServletResponse response;

    @Captor
    ArgumentCaptor<String> responseBodyCaptor;

    @BeforeEach
    void setting() {
        MockitoAnnotations.openMocks(this);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void JsonView_출력_양식_테스트() throws Exception {
        // given
        PrintWriter mockWriter = mock(PrintWriter.class);
        given(response.getWriter())
            .willReturn(mockWriter);

        JsonView jsonView = new JsonView();
        HashMap<String, Object> model = new HashMap<>();
        model.put("key1", "value1");
        model.put("key2", List.of("푸우", "주노"));

        String expect = "{\n"
            + "  \"key1\" : \"value1\",\n"
            + "  \"key2\" : [ \"푸우\", \"주노\" ]\n"
            + "}";

        // when
        jsonView.render(model, null, response);

        // then
        verify(response, times(1)).getWriter();
        verify(mockWriter).write(responseBodyCaptor.capture());
        assertThat(responseBodyCaptor.getValue()).isEqualTo(expect);
    }

    @Test
    void 전달_인자가_하나일_경우_data_만_출력한다() throws Exception {

        // given
        PrintWriter mockWriter = mock(PrintWriter.class);
        given(response.getWriter())
            .willReturn(mockWriter);

        JsonView jsonView = new JsonView();
        HashMap<String, Object> model = new HashMap<>();
        model.put("key1", "value1");

        // when
        jsonView.render(model, null, response);

        // then
        verify(response, times(1)).getWriter();
        verify(mockWriter).write(responseBodyCaptor.capture());
        assertThat(responseBodyCaptor.getValue()).isEqualTo("\"value1\"");

    }

}
