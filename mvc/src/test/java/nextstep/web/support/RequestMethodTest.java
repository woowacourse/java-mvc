package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    @DisplayName("값으로 RequestMethod를 찾는다.")
    void from() {
        final String get = "GET";

        final RequestMethod requestMethod = RequestMethod.from(get);

        assertThat(requestMethod).isEqualTo(RequestMethod.GET);
    }

    @Test
    @DisplayName("값으로 RequestMethod를 찾지 못하면 예외가 발생한다.")
    void fromWithWrongMethod() {
        final String get = "invalid";

        assertThatThrownBy(() -> RequestMethod.from(get))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
