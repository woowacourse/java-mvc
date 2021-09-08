package nextstep.mvc.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

import static org.assertj.core.api.Assertions.assertThat;

class JspViewResolverTest {

    @Test
    @DisplayName("jsp 파일인 경우")
    void resolveViewName() {
        // given
        String viewName = "/login.jsp";
        JspViewResolver jspViewResolver = new JspViewResolver();

        // when
        View view = jspViewResolver.resolveViewName(viewName);

        // then
        assertThat(view).isInstanceOf(JspView.class);
    }

    @Test
    @DisplayName("jsp 파일이 아닌 경우")
    void resolveViewNameFail() {
        // given
        String viewName = "/login.html";
        JspViewResolver jspViewResolver = new JspViewResolver();

        // when
        View view = jspViewResolver.resolveViewName(viewName);

        // then
        assertThat(view).isNull();
    }

}
