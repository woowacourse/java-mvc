package nextstep.web.support;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    @DisplayName("적절하지 않은 메서드를 입력할 경우 에러를 발생한다.")
    void requestMethodNotSupported() {
        assertThatThrownBy(() -> RequestMethod.from(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("적절한 Request Method를 찾을 수 없습니다.");
    }
}