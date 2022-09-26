package nextstep.mvc.controller.tobe.resolvers;

import static org.assertj.core.api.Assertions.*;

import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JspResolverTest {

    @Test
    @DisplayName("주어진 object가 문자열이라면 support 가능하다")
    void supports() {
        // given
        JspResolver resolver = new JspResolver();
        // when
        boolean isSupports = resolver.supports("hello");

        // then
        assertThat(isSupports).isTrue();
    }

    @Test
    @DisplayName("주어진 문자열을 Jsp view로 전환한다.")
    void resolve() {
        // given
        JspResolver resolver = new JspResolver();
        // when
        ModelAndView modelAndView = resolver.resolve("hello");
        // then
        assertThat(modelAndView.getViewName()).isEqualTo("hello");
    }
}
