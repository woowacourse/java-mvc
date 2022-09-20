package nextstep.mvc.controller.tobe.mappings;

import static org.assertj.core.api.Assertions.*;

import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @Test
    void equals() {
        // given
        HandlerKey keyA = new HandlerKey("/hello", RequestMethod.GET);
        HandlerKey keyB = new HandlerKey("/hello", RequestMethod.GET);
        // when
        boolean isEqual = keyA.equals(keyB);
        // then
        assertThat(isEqual).isTrue();
    }
}
