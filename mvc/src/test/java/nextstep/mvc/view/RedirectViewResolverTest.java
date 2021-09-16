package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RedirectViewResolverTest {

    private RedirectViewResolver redirectViewResolver;

    @BeforeEach
    void setUp() {
        redirectViewResolver = new RedirectViewResolver();
    }

    @DisplayName("Jsp 관련 뷰인지 확인한다.")
    @Test
    void supports() {
        boolean isSupport = redirectViewResolver.supports("redirect:/");
        boolean isNotSupport = redirectViewResolver.supports("index.htm");

        assertThat(isSupport).isTrue();
        assertThat(isNotSupport).isFalse();
    }

    @DisplayName("뷰 이름으로 뷰 객체를 반환한다.")
    @Test
    void resolveViewName() {
        View view = redirectViewResolver.resolveViewName("redirect:/");

        assertThat(view).isInstanceOf(RedirectView.class);
    }
}