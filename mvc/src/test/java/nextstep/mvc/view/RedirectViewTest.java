package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RedirectViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> model;

    @BeforeEach
    void setUp(){
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        model = new HashMap<>();
    }

    @DisplayName("redirect가 되었는지 확인하다.")
    @Test
    void render() throws Exception {
        RedirectView redirectView = new RedirectView("redirect:/");

        redirectView.render(model, request, response);

        verify(response).sendRedirect("/");
    }
}
