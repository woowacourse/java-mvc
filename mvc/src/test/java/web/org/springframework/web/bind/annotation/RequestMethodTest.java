package web.org.springframework.web.bind.annotation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMethodTest {

    @Test
    void 메서드_이름에_맞는_RequestMethod_반환() {
        String methodName = "get";
        RequestMethod expected = RequestMethod.GET;

        assertThat(RequestMethod.from(methodName)).isEqualTo(expected);
    }

    @Test
    void 존재하지_않는_메서드면_예외() {
        String invalidMethodName = "get??";

        Assertions.assertThatThrownBy(() -> RequestMethod.from(invalidMethodName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
