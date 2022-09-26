package nextstep.mvc.controller.tobe.resolvers;

import static org.assertj.core.api.Assertions.*;

import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.JsonTest;

class JsonResolverTest {

    @Test
    @DisplayName("nextstep 패키지가 아니어야 support 할 수 있다")
    void supports() {
        // given
        JsonResolver resolver = new JsonResolver();
        // when
        boolean isSupports = resolver.supports(new JsonTest());
        // then
        assertThat(isSupports).isTrue();
    }

    @Test
    @DisplayName("이미 알고있는(nextstep) 객체라면 suuport 할 수 없다")
    void supportsFailsOnKnownClass() {
        // given
        JsonResolver resolver = new JsonResolver();
        // when
        ModelAndView modelAndView = new ModelAndView(new JsonView());
        boolean isSupports = resolver.supports(modelAndView);
        // then
        assertThat(isSupports).isFalse();
    }

    @Test
    @DisplayName("object 객체를 modelAndView로 전환한다")
    void resolve() {
        // given
        JsonResolver resolver = new JsonResolver();
        // when
        ModelAndView modelAndView = resolver.resolve(new ModelAndView(new JsonView()));
        // then
        assertThat(modelAndView).isNotNull();
    }
}
