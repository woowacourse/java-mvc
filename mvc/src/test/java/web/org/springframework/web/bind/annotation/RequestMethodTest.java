package web.org.springframework.web.bind.annotation;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;

class RequestMethodTest {

    @ParameterizedTest
    @ValueSource(strings = {"GET", "POST", "PUT", "DELETE", "HEAD", "PATCH", "OPTIONS", "TRACE"})
    void String을_알맞는_RequestMethod로_변환_할_수_있다(final String method) {
        // when, then
        assertThatCode(() -> RequestMethod.from(method))
                .doesNotThrowAnyException();
    }
}
