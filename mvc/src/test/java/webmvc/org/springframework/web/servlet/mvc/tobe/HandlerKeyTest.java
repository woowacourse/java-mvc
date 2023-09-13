package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import web.org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerKeyTest {

    @Test
    void 생성자는_url과_HTTP_메서드를_전달하면_HandlerKey를_초기화한다() {
        assertDoesNotThrow(() -> new HandlerKey("/", RequestMethod.GET));
    }

    @Test
    void 생성자는_유효하지_않은_url을_전달하면_예외가_발생한다() {
        final String invalidUrl = null;

        assertThatThrownBy(() -> new HandlerKey(invalidUrl, RequestMethod.GET))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 URL 입니다.");
    }

    @Test
    void 생성자는_유효하지_않은_RequestMethod를_전달하면_예외가_발생한다() {
        final RequestMethod invalidRequestMethod = null;

        assertThatThrownBy(() -> new HandlerKey("/", invalidRequestMethod))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 HTTP Method 입니다.");
    }

    @Test
    void equals_메서드는_동일한_HandlerKey라면_true를_반환한다() {
        final HandlerKey first = new HandlerKey("/", RequestMethod.GET);
        final HandlerKey second = new HandlerKey("/", RequestMethod.GET);

        assertThat(first).isEqualTo(second);
    }

    @Test
    void equals_메서드는_동일한_HandlerKey가_아니라면_false를_반환한다() {
        final HandlerKey first = new HandlerKey("/", RequestMethod.GET);
        final HandlerKey second = new HandlerKey("/", RequestMethod.POST);

        assertThat(first).isNotEqualTo(second);
    }
}
