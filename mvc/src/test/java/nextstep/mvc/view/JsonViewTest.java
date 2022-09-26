package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import nextstep.fixtures.HttpServletFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonViewTest {
    @Test
    @DisplayName("json view를 렌더링한다")
    void render() throws IOException {
        // given
        View view = new JsonView();
        Map<String, Object> model = new HashMap<>();
        model.put("hello", "world");

        HttpServletResponse response = HttpServletFixtures.createResponse();
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        // when & then
        assertThatNoException().isThrownBy(
                () -> view.render(model, null, response)
        );
    }
}
