package webmvc.org.springframework.web.servlet.view;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JspViewTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void viewName이_리다이렉트을_명시한다면_redirect한다() throws IOException {
        // given
        JspView jspView = new JspView("redirect:test");

        // when
        jspView.render(Map.of(), request, response);

        // then
        then(response)
                .should(times(1))
                .sendRedirect(anyString());
    }

    @Test
    void 요청의_속성을_저장하고_requestDispatcher로_forward한다() throws ServletException, IOException {
        // given
        given(request.getRequestDispatcher("test"))
                .willReturn(requestDispatcher);

        Map<String, String> model = Map.of("test", "model");
        JspView jspView = new JspView("test");

        // when
        jspView.render(model, request, response);
        
        // then
        then(request)
                .should(times(1))
                .setAttribute("test", "model");
        then(requestDispatcher)
                .should(times(1))
                .forward(request, response);
    }
}
