package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("입력한 문자열과 동일한 RequestMethod 객체 조회")
    @Test
    void from() {
        assertThat(RequestMethod.from("get")).isEqualTo(RequestMethod.GET);
    }

    @DisplayName("존재하지 않는 RequestMethod 조회 시 예외 발생")
    @Test
    void fromAboutNotExist() {
        assertThatThrownBy(() -> RequestMethod.from("invalid method"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("정의되지 않은 메서드입니다.");
    }
}
