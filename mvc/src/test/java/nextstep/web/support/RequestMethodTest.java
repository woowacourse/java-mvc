package nextstep.web.support;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMethodTest {

    @DisplayName("파라미터로 들어온 methodName과 일치하는 enum 반환한다.")
    @Test
    void from() {
        final String Get = "GET";

        final RequestMethod requestMethod = RequestMethod.from(Get);

        assertThat(requestMethod).isSameAs(RequestMethod.GET);
    }

    @DisplayName("대소문자를 무시하고 일치하는 enum을 반환한다.")
    @Test
    void from_IgnoreCase() {
        final String Post = "pOsT";

        final RequestMethod requestMethod = RequestMethod.from(Post);

        assertThat(requestMethod).isSameAs(RequestMethod.POST);
    }
}
