package web.org.springframework.web.bind.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import web.org.springframework.web.bind.exception.InvalidRequestMethodException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestMethodTest {

    @Test
    @DisplayName("문자열로 주어진 request method를 RequestMethod로 변환한다.")
    void stringToRequestMethod() {
        // given
        final String requestMethod = "get";

        // when
        final RequestMethod actual = RequestMethod.from(requestMethod);

        // then
        assertThat(actual).isEqualTo(RequestMethod.GET);
    }

    @Test
    @DisplayName("지원하지 않는 request method가 문자열로 주어진다면 예외가 발생한다.")
    void invalidStringToRequestMethod() {
        // given
        final String requestMethod = "invalid request method";

        // when & throw
        assertThatThrownBy(() -> RequestMethod.from(requestMethod))
                .isInstanceOf(InvalidRequestMethodException.class)
                .hasMessage("지원하지 않는 Request Method 입니다.");
    }
}
