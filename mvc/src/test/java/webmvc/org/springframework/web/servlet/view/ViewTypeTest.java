package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static webmvc.org.springframework.web.servlet.view.ViewType.JSP;

import org.junit.jupiter.api.Test;

class ViewTypeTest {

    @Test
    void String_값으로_타입을_반환한다() {
        // given
        String input = "jsp";

        // when
        ViewType result = ViewType.from(input);

        // then
        assertThat(result).isEqualTo(JSP);
    }

    @Test
    void 타입이_없으면_예외가_발생한다() {
        // given
        String input = "index.html";

        // expect
        assertThatThrownBy(() -> ViewType.from(input))
                .isInstanceOf(RuntimeException.class);
    }
}
