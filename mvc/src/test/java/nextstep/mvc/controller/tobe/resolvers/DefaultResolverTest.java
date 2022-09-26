package nextstep.mvc.controller.tobe.resolvers;

import static org.assertj.core.api.Assertions.*;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultResolverTest {

    @Test
    @DisplayName("ModelAndView 객체의 support 여부를 확인한다")
    void supports() {
        // given
        DefaultResolver resolver = new DefaultResolver();
        Object object = new ModelAndView(new JsonView());
        // when
        boolean isSupports = resolver.supports(object);
        // then
        assertThat(isSupports).isTrue();
    }

    @Test
    @DisplayName("String 객체는 support 할 수 없다")
    void supportsFalseIfNotModelAndView() {
        // given
        DefaultResolver resolver = new DefaultResolver();
        Object object = "not-supportable";
        // when
        boolean isSupports = resolver.supports(object);
        // then
        assertThat(isSupports).isFalse();
    }

    @Test
    @DisplayName("object 객체를 modelandview 객체로 캐스팅해 반환한다")
    void resolve() {
        // given
        DefaultResolver resolver = new DefaultResolver();
        Object object = new ModelAndView(new JsonView());

        // when
        ModelAndView modelAndView = resolver.resolve(object);

        // then
        assertThat(modelAndView).isNotNull();
    }
}
