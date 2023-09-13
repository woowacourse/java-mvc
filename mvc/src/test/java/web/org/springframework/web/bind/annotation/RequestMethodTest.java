package web.org.springframework.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RequestMethodTest {

    @Test
    void from_메서드는_지원하는_메서드_이름을_전달하면_해당_메서드를_반환한다() {
        final RequestMethod actual = RequestMethod.from("GET");

        assertThat(actual).isEqualTo(RequestMethod.GET);
    }

    @Test
    void from_메서드는_지원하지_않는_메서드_이름을_전달하면_예외가_발생한다() {
        final String invalidMethodName = "invalid";

        assertThatThrownBy(() -> RequestMethod.from(invalidMethodName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원하지 않는 HTTP Method 입니다.");
    }
}
