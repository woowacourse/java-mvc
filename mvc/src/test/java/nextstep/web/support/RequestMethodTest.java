package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("valueOf 메서드의 동작을 확인한다.")
    @Test
    void valueOf() {
        RequestMethod method = RequestMethod.valueOf("GET");

        assertThat(method).isEqualTo(RequestMethod.GET);
    }

}
