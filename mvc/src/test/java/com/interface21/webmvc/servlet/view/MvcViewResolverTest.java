package com.interface21.webmvc.servlet.view;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.interface21.webmvc.servlet.view.type.JspView;
import org.junit.jupiter.api.Test;

class MvcViewResolverTest {

    @Test
    void JSP_확장자를_resolve_할_수_있다() {
        String jspFileName = "a.jsp";
        ViewResolver viewResolver = new MvcViewResolver();

        assertThat(viewResolver.resolveViewName(jspFileName))
                .isExactlyInstanceOf(JspView.class);
    }

    @Test
    void 설정되지_않은_확장자는_resolve_할_수_없다() {
        String unknownExtensionFileName = "a.???";
        ViewResolver viewResolver = new MvcViewResolver();

        assertThatThrownBy(() -> viewResolver.resolveViewName(unknownExtensionFileName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("매핑 불가능한 View 확장자(%s) 입니다.".formatted(".???"));
    }
}
