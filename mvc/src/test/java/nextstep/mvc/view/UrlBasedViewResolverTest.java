package nextstep.mvc.view;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UrlBasedViewResolverTest {

    private UrlBasedViewResolver urlBasedViewResolver;

    @BeforeEach
    void setUp() {
        urlBasedViewResolver = new UrlBasedViewResolver();
    }

    @DisplayName("Jsp 관련 뷰인지 확인한다.")
    @Test
    void supports() {
        boolean isSupport = urlBasedViewResolver.supports("redirect:/");
        boolean isNotSupport = urlBasedViewResolver.supports("index.htm");

        assertThat(isSupport).isTrue();
        assertThat(isNotSupport).isFalse();
    }

    @DisplayName("뷰 이름으로 뷰 객체를 반환한다.")
    @Test
    void resolveViewName() {
        View view = urlBasedViewResolver.resolveViewName("redirect:/");

        assertThat(view).isInstanceOf(RedirectView.class);
    }
}