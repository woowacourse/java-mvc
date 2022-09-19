package nextstep.mvc.view;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JspViewTest {

    @Test
    void render_forwardViewName() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);
        String viewName = "/";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = new HashMap<>();

        // when
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);
        jspView.render(model, request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void render_redirectViewName() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        String viewName = "redirect:/";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = new HashMap<>();

        // when
        jspView.render(model, request, response);

        // then
        verify(response).sendRedirect("/");
    }

    @Test
    void render_WithModel() throws Exception {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = Mockito.mock(RequestDispatcher.class);

        String viewName = "/login";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = new HashMap<>();
        model.put("id", "gugu");
        model.put("pw", "password");

        // when
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);
        jspView.render(model, request, response);

        // then
        for(String key : model.keySet()){
            verify(request).setAttribute(key, model.get(key));
        }
        verify(requestDispatcher).forward(request, response);
    }
}
