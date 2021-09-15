package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JsonViewTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @DisplayName("JsonView render Test")
    @MethodSource("renderTest")
    public void jsonViewRenderTest(Map<String, ?> model, String expected) throws Exception {
        JsonView jsonView = new JsonView();
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        doNothing().when(response).setContentType(Mockito.anyString());
        when(response.getWriter()).thenReturn(writer);

        jsonView.render(model, request, response);
        writer.flush();

        assertThat(stringWriter.toString()).isEqualTo(expected);
    }

    private static Stream<Arguments> renderTest() throws JsonProcessingException {
        Map<String, Object> testCase1 = new HashMap<>();
        Map<String, Object> testCase2 = new HashMap<>();

        testCase1.put("user", new User(1L, "bepoz"));
        testCase2.put("user", new User(1L, "bepoz"));
        testCase2.put("user2", new User(2L, "noneg"));

        return Stream.of(
                Arguments.of(testCase1, objectMapper.writeValueAsString(testCase1.get("user"))),
                Arguments.of(testCase2, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(testCase2))
        );
    }

    public static class User {
        private Long id;
        private String name;

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}