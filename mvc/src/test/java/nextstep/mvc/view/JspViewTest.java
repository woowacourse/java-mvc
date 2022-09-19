package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import nextstep.mvc.exception.NotFoundException;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void render() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        JspView jspView = JspView.from("");

        assertThatThrownBy(() -> jspView.render(new HashMap<>(), request, response))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("JSP not found.");
    }
}
