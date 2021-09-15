package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import nextstep.mvc.MockHttpServletResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class JsonViewTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("parametersRenderTest")
    void render(Map<String, Object> model, String expected) throws Exception {
        JsonView jsonView = new JsonView();

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        jsonView.render(model, null, mockHttpServletResponse);

        var writer = (MockHttpServletResponse.Writer) mockHttpServletResponse.getWriter();

        assertThat(writer.getValue()).isEqualTo(expected);
    }

    private static Stream<Arguments> parametersRenderTest() throws JsonProcessingException {
        Map<String, Object> test1 = new HashMap<>();
        Map<String, Object> test2 = new HashMap<>();

        test1.put("test1", new TestObject());
        test2.put("test1", new TestObject());
        test2.put("test2", new TestObject());

        return Stream.of(
            Arguments.of(test1, objectMapper.writeValueAsString(test1.get("test1"))),
            Arguments.of(test2, objectMapper.writeValueAsString(test2))
        );
    }

    public static class TestObject {
        int a = 1;
        int b = 2;

        public int getA() {
            return a;
        }

        public int getB() {
            return b;
        }
    }
}
