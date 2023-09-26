package webmvc.org.springframework.web.servlet.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void Jsp_View_는_응답을_내려준다() throws Exception {
        // given
        final var jspView = new JspView("/login.jsp");
        final var requestDispatcher = mock(RequestDispatcher.class);

        // when
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        jspView.render(new HashMap<>(), request, response);

        // then
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void Jsp_View_는_요청에_속성을_담아_응답을_내려준다() throws Exception {
        // given
        final var jspView = new JspView("/login.jsp");
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var model = Map.of("username", "blackcat", "password", "1234");

        // when
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        jspView.render(model, request, response);

        // then
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute("username", "blackcat");
        verify(request).setAttribute("password", "1234");
    }

    @Test
    void Jsp_View_는_redirect_prefix_가_있을_시_리다이렉션_응답을_내려준다() throws Exception {
        // given
        final var jspView = new JspView("redirect:/401.jsp");

        // when
        jspView.render(new HashMap<>(), request, response);

        // then
        verify(response).sendRedirect("/401.jsp");
    }
}
