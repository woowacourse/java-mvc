package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Disabled("redirect가 되었는지 확인하다.")
    @Test
    void render() throws Exception {
        RedirectView redirectView = new RedirectView("redirect:/");

        redirectView.render(model, request, response);

        verify(response).sendRedirect("/");
    }
}