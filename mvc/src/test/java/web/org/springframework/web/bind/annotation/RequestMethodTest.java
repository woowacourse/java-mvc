package web.org.springframework.web.bind.annotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import web.org.springframework.exception.BindException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestMethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "b", "c"})
    void throwExceptionWhenValueIsNotExists(final String notExistsValue) {
        assertThatThrownBy(() -> RequestMethod.from(notExistsValue))
                .isInstanceOf(BindException.class);
    }

    @Test
    void throwExceptionWhenValueIsNull() {
        assertThatThrownBy(() -> RequestMethod.from(null))
                .isInstanceOf(BindException.class);
    }
}
