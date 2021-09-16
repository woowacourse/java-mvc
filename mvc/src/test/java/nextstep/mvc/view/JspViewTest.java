package nextstep.mvc.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JspViewTest {

    HttpServletRequest request;
    HttpServletResponse response;
    RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @DisplayName("path = index.html : JspView 객체 잘 실행된다.")
    @Test
    void render() throws Exception {
        // given
        when(request.getRequestDispatcher("index.html")).thenReturn(requestDispatcher);

        Map<String, ?> model = new HashMap<>();
        View view = new JspView("index.html");

        // when
        view.render(model, request, response);

        // then
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @DisplayName("path = redirect: todo.html : JspView 객체 잘 실행된다.")
    @Test
    void renderRedirect() throws Exception {
        // given
        String redirectName = "redirect: todo.html";
        String location = " todo.html";
        doNothing().when(response).sendRedirect(location);

        Map<String, ?> model = new HashMap<>();
        View view = new JspView(redirectName);

        // when
        view.render(model, request, response);

        // then
        verify(response, times(1)).sendRedirect(location);
    }
}