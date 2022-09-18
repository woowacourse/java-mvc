package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    void 해당하는_RequestMethod가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> RequestMethod.from("None"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 HttpMethod가 존재하지 않습니다.");
    }

    @Test
    void 해당하는_RequestMethod를_찾는다() {
        final RequestMethod actual = RequestMethod.from("GET");

        assertThat(actual).isEqualTo(RequestMethod.GET);
    }
}
