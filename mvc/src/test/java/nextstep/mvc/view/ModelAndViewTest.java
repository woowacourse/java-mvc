package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class ModelAndViewTest {

    @Test
    @DisplayName("모델에 Attribute를 추가한다.")
    void addAttribute() {
        // given
        ModelAndView modelAndView = new ModelAndView(new JspView(""));

        // when
        final String attributeName = "attribute";
        modelAndView.addAttribute(attributeName, "value");
        final Object attribute = modelAndView.getAttribute(attributeName);

        // then
        assertThat((String)attribute).isEqualTo("value");
    }

    @Test
    @DisplayName("render를 호출했을 때 Jsp 경로가 아니면 request의 getRequestDispatcher를 호출한다.")
    void render_notJsp() {
        // given
        final String viewName = "";
        final ModelAndView modelAndView = new ModelAndView(new JspView(viewName));

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

        // when
        modelAndView.render(request, response);

        // then
        verify(request, times(1)).getRequestDispatcher(viewName);
    }
}
