package nextstep.mvc.view.resolver;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.RedirectView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UrlBasedViewResolverTest {

    private final ViewResolver viewResolver = new UrlBasedViewResolver();

    @DisplayName("redirect:가 붙은 url은 RedirectView를 반환한다.")
    @Test
    void resolveViewNameWhenRedirect() {
        // given
        String viewName = "redirect:view.jsp";

        // when
        View view = viewResolver.resolveViewName(viewName);

        // then
        assertThat(view).isInstanceOf(RedirectView.class);
    }

    @DisplayName("뒤에 .jsp가 붙은 url은 JSPView를 반환한다.")
    @Test
    void resolveViewNameWhenForwardJSP() {
        // given
        String viewName = "view.jsp";

        // when
        View view = viewResolver.resolveViewName(viewName);

        // then
        assertThat(view).isInstanceOf(JspView.class);
    }

    @DisplayName("viewName이 매칭되지 않는 경우 JsonView를 반환한다.")
    @Test
    void resolveViewNameWhenJSON() {
        // given
        String viewName = "";

        // when
        View view = viewResolver.resolveViewName(viewName);

        // then
        assertThat(view).isInstanceOf(JsonView.class);
    }
}