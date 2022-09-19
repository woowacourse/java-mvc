package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspViewTest {

    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

    @DisplayName("render 가 정상적으로 forward 되는지 확인한다.")
    @Test
    void render_forwardViewName() throws Exception {
        // given
        String viewName = "/";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = new HashMap<>();

        // when
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);
        jspView.render(model, request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @DisplayName("redirect: 요청시 render 가 정상적으로 forward 되는지 확인한다.")
    @Test
    void render_redirectViewName() throws Exception {
        // given
        String viewName = "redirect:/";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = new HashMap<>();

        // when
        jspView.render(model, request, response);

        // then
        verify(response).sendRedirect("/");
    }

    @DisplayName("model 반환시 render 가 정상적으로 forward 되는지 확인한다.")
    @Test
    void render_WithModel() throws Exception {
        String viewName = "/login";
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = new HashMap<>();
        model.put("id", "gugu");
        model.put("pw", "password");

        // when
        when(request.getRequestDispatcher(viewName)).thenReturn(requestDispatcher);
        jspView.render(model, request, response);

        // then
        for (String key : model.keySet()) {
            verify(request).setAttribute(key, model.get(key));
        }
        verify(requestDispatcher).forward(request, response);
    }

}

