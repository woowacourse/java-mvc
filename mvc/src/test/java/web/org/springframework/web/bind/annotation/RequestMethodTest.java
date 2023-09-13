package web.org.springframework.web.bind.annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import web.org.springframework.web.bind.exception.NotAllowedMethodException;

class RequestMethodTest {

    @Test
    @DisplayName("methodName에 해당하는 RequestMethod를 찾는다.")
    void findRequestMethod() {
        // given
        final String methodName = "GET";

        // when
        RequestMethod findRequestMethod = RequestMethod.findRequestMethod(methodName);

        // then
        assertThat(findRequestMethod).isEqualTo(RequestMethod.GET);
    }

    @Test
    @DisplayName("methodName에 해당하는 RequestMethod이 없을 경우 예외가 발생한다.")
    void throws_notExistMethodName() {
        // given
        final String notExistMethodName = "notExistMethodName";

        // when & then
        assertThatThrownBy(() -> RequestMethod.findRequestMethod(notExistMethodName))
                .isInstanceOf(NotAllowedMethodException.class)
                .hasMessage("허용되지 않은 Http Method입니다.");
    }
}
