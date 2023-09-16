package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JspViewTest {

    @Test
    void 리다이렉트뷰_이름은_prefix를_제거한다() {
        //given
        JspView jspView = new JspView("redirect:/");

        //when
        String actual = jspView.getViewName();

        //then
        assertThat(actual).isEqualTo("/");
    }

    @Test
    void 리다이렉트가_아닌경우_그대로_반환한다() {
        //given
        JspView jspView = new JspView("/index.jsp");

        //when
        String actual = jspView.getViewName();

        //then
        assertThat(actual).isEqualTo("/index.jsp");
    }

    @Test
    void isRedirectView() {
        //given
        JspView jspView = new JspView("redirect:/");

        //when
        boolean actual = jspView.isRedirectView();

        //then
        assertThat(actual).isTrue();
    }
}
