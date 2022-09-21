package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    void reqruest_method를_찾는다() {
        // given
        String method = "get";

        // when
        RequestMethod requestMethod = RequestMethod.from(method);

        // then
        assertThat(requestMethod).isEqualTo(RequestMethod.GET);
    }

}
