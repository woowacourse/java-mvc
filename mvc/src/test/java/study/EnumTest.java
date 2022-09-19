package study;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.Test;

public class EnumTest {

    @Test
    void valueOf() {
        assertThatThrownBy(() -> RequestMethod.valueOf("야호"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
