package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.mvc.common.exception.InvalidRequestMethodException;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @Test
    void find() {
        RequestMethod requestMethod = RequestMethod.find("get");

        assertThat(requestMethod).isEqualTo(RequestMethod.GET);
    }

    @Test
    void findInvalidValueThrownException() {
        assertThatThrownBy(() -> RequestMethod.find("aaa"))
                .isInstanceOf(InvalidRequestMethodException.class)
                .hasMessage("잘못된 요청 메서드 입니다.");
    }
}