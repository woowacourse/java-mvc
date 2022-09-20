package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RequestMethodTest {

    @ParameterizedTest(name = "RequestMethod.from은 {0} 전달 시 {1}을 반환한다")
    @CsvSource(value = {"get,GET", "GET,GET", "Get,GET",
            "head,HEAD", "post, POST", "put,PUT", "patch,PATCH", "delete,DELETE", "options,OPTIONS", "trace,TRACE"})
    @DisplayName("sampleTest")
    void parameterizedTest(String rawRequestMethod, RequestMethod expected) {
        // given
        final var requestMethod = rawRequestMethod;

        // when
        final RequestMethod actual = RequestMethod.from(requestMethod);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
