package nextstep.mvc.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BeanNameParserUtilsTest {

    @DisplayName("이름 첫번째 글자를 소문자로 변환한다.")
    @Test
    void toLowerFirstChar() {
        String origin = "HiThisIsCorgi";
        String after = BeanNameParserUtils.toLowerFirstChar(origin);
        assertThat(after).isEqualTo("hiThisIsCorgi");
    }
}