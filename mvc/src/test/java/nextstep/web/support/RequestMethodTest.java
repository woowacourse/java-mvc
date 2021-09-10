package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("메서드 이름을 통해 메서드 객체를 가져올 수 있다.")
    @Test
    void valueOf() {
        // given
        String name = "get";

        // when
        RequestMethod method = RequestMethod.valueOf(name.toUpperCase());

        // then
        assertThat(method).isSameAs(RequestMethod.GET);
    }
}
