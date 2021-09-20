package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {

    private HttpServletResponse response;
    private HttpServletRequest request;
    private JsonView jsonView = new JsonView();

    @BeforeEach
    void setUp() throws IOException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("model이 하나일 때 테스트")
    @Test
    void onlyOneOnModel() throws Exception {
        //given
        Map<String, String> model = new HashMap<>();
        String one = "one";
        model.put("one", one);
        DummyWriter dummyWriter = new DummyWriter(System.out);
        when(response.getWriter()).thenReturn(dummyWriter);

        //when
        jsonView.render(model, request, response);

        //then
        assertThat(dummyWriter.getData()).isEqualTo("\"one\"");
    }

    @DisplayName("model이 여러 개 일 때 테스트")
    @Test
    void SeveralModels() throws Exception{
        //given
        Map<String, String> model = new HashMap<>();
        String one = "one";
        String two = "two";
        model.put("one", one);
        model.put("two", two);
        DummyWriter dummyWriter = new DummyWriter(System.out);
        when(response.getWriter()).thenReturn(dummyWriter);

        //when
        jsonView.render(model, request, response);

        //then
        assertThat(dummyWriter.getData()).isEqualTo("{\"one\":\"one\",\"two\":\"two\"}");
    }

}