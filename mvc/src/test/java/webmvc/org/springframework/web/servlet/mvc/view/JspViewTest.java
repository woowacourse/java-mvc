package webmvc.org.springframework.web.servlet.mvc.view;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JspViewTest {

    @Test
    void 생성자는_유효한_값을_전달하면_JspView를_초기화한다() {
        assertDoesNotThrow(() -> new JspView("/hello.jsp"));
    }

    @Test
    void 생성자는_유효하지_않은_viewName을_전달하면_예외가_발생한다() {
        final String invalidViewName = null;

        assertThatThrownBy(() -> new JspView(invalidViewName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이동할 페이지를 입력해주세요.");
    }

    @Test
    void 생성자는_jsp_접미사가_없는_viewName을_전달하면_예외가_발생한다() {
        final String notJspViewName = "hello.html";

        assertThatThrownBy(() -> new JspView(notJspViewName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("올바르지 않은 jsp 이름입니다.");
    }

    @Test
    void render_메서드는_리다이렉트인_경우_호출하면_해당_요청을_리다이렉트한다() throws Exception {
        final JspView jspView = new JspView("redirect:/hello.jsp");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        willDoNothing().given(response).sendRedirect(anyString());

        jspView.render(new HashMap<>(), request, response);

        then(response).should(times(1)).sendRedirect(anyString());
    }

    @Test
    void render_메서드는_리다이렉트가_아닌_경우_해당_요청을_포워드한다() throws Exception {
        final JspView jspView = new JspView("hello.jsp");
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        given(request.getRequestDispatcher(anyString())).willReturn(requestDispatcher);
        willDoNothing().given(requestDispatcher).forward(any(HttpServletRequest.class), any(HttpServletResponse.class));

        jspView.render(new HashMap<>(), request, response);

        then(requestDispatcher).should(times(1))
                               .forward(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }
}
