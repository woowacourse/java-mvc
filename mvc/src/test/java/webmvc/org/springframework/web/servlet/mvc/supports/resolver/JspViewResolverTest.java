package webmvc.org.springframework.web.servlet.mvc.supports.resolver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.View;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class JspViewResolverTest {

    @Test
    void 생성자는_호출하면_JspViewResolver를_초기화한다() {
        assertDoesNotThrow(JspViewResolver::new);
    }

    @Test
    void supports_메서드는_지원하는_접미사가_있으면_true를_반환한다() {
        final JspViewResolver viewResolver = new JspViewResolver();

        final boolean actual = viewResolver.supports(null, "/index.jsp");

        assertThat(actual).isTrue();
    }

    @Test
    void supports_메서드는_지원하는_접미사가_없으면_false를_반환한다() {
        final JspViewResolver viewResolver = new JspViewResolver();

        final boolean actual = viewResolver.supports(null, "/index.html");

        assertThat(actual).isFalse();
    }

    @Test
    void resolve_메서드는_viewName에_맞는_View를_초기화해_반환한다() {
        final JspViewResolver viewResolver = new JspViewResolver();

        final View actual = viewResolver.resolve("/index.jsp");

        assertThat(actual).isExactlyInstanceOf(JspView.class);
    }
}
