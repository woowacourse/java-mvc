package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    void method를_통해_RequestMethod를_찾는다() {
        // given
        String method = "GET";

        // when
        RequestMethod requestMethod = RequestMethod.of(method);

        // then
        assertThat(requestMethod).isEqualTo(RequestMethod.GET);
    }

    @Test
    void method를_통해_RequestMethod를_찾지_못하면_예외를_던진다() {
        // given
        String invalidMethod = "GOT";

        // when & then
        assertThatThrownBy(() -> RequestMethod.of(invalidMethod))
                .isInstanceOf(RuntimeException.class);
    }
}
