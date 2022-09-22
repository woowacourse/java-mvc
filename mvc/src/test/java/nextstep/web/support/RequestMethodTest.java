package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RequestMethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"GET", "get"})
    @DisplayName("값으로 RequestMethod를 찾는다.")
    void from(String method) {
        final RequestMethod requestMethod = RequestMethod.from(method);

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
