package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    void 해당하는_RequestMethod가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> RequestMethod.valueOf("None"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 해당하는_RequestMethod를_찾는다() {
        final RequestMethod actual = RequestMethod.valueOf("GET");

        assertThat(actual).isEqualTo(RequestMethod.GET);
    }
}
