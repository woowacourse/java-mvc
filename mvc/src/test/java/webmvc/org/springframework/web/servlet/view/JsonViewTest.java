package webmvc.org.springframework.web.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestUser;

import java.io.PrintWriter;
import java.util.Map;

class JsonViewTest {

    @Test
    @DisplayName("model 에 담긴 값을 Json 형식으로 직렬화한다.")
    void serializeResponseBodyToJson() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        final var model = Map.of(
                "user", new TestUser("a", "1", "a@gmail.com"),
                "user2", new TestUser("b", "2", "b@gmail.com")
        );
        new JsonView().render(model, request, response);

        verify(printWriter).print("{\n" +
                "  \"user\" : {\n" +
                "    \"account\" : \"a\",\n" +
                "    \"password\" : \"1\",\n" +
                "    \"email\" : \"a@gmail.com\"\n" +
                "  },\n" +
                "  \"user2\" : {\n" +
                "    \"account\" : \"b\",\n" +
                "    \"password\" : \"2\",\n" +
                "    \"email\" : \"b@gmail.com\"\n" +
                "  }\n" +
                "}");
        verify(printWriter).flush();
        verify(printWriter).close();
    }

}
