package nextstep.mvc.resolver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.mvc.view.JspView;
import nextstep.mvc.view.View;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceViewResolverTest {

    @Test
    @DisplayName("jsp 파일인 경우")
    void resolveViewName() {
        // given
        String viewName = "/login.jsp";
        ResourceViewResolver resourceViewResolver = new ResourceViewResolver();

        // when
        View view = resourceViewResolver.resolveViewName(viewName);

        // then
        assertThat(view).isInstanceOf(JspView.class);
    }

    @Test
    @DisplayName("jsp 파일이 아닌 경우")
    void resolveViewNameFail() {
        // given
        String viewName = "/login.html";
        ResourceViewResolver resourceViewResolver = new ResourceViewResolver();

        // when
        View view = resourceViewResolver.resolveViewName(viewName);

        // then
        assertThat(view).isNull();
    }

}
