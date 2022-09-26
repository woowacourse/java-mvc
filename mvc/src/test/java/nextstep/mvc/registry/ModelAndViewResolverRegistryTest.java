package nextstep.mvc.registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import nextstep.mvc.ModelAndViewResolver;
import nextstep.mvc.controller.tobe.resolvers.DefaultResolver;
import nextstep.mvc.controller.tobe.resolvers.JspResolver;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModelAndViewResolverRegistryTest {

    @Test
    @DisplayName("리졸버를 추가한다")
    void addResolver() {
        // given
        ModelAndViewResolverRegistry registry = new ModelAndViewResolverRegistry();
        // when & then
        assertThatNoException().isThrownBy(
                () -> registry.addModelAndViewResolver(new JspResolver())
        );
    }

    @Test
    @DisplayName("리졸버를 찾는다")
    void findModelAndViewResolver() {
        // given
        ModelAndViewResolverRegistry registry = new ModelAndViewResolverRegistry();
        registry.addModelAndViewResolver(new DefaultResolver());

        // when
        Object object = new ModelAndView(new JspView("viewName"));
        ModelAndViewResolver resolver = registry.findModelAndViewResolver(object);

        // then
        assertThat(resolver).isInstanceOf(DefaultResolver.class);
    }
}
