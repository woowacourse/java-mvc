package nextstep.mvc.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.web.support.RequestMethod.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HandlerKey 테스트")
class HandlerKeyTest {

    private static final String URL = "url";

    @DisplayName("url과 requestMethod가 모두 같으면 equals&hashCode도 같다.")
    @Test
    void equalsAndHashCode() {
        // given
        // when
        // then
        assertThat(new HandlerKey(URL, POST))
                .isEqualTo(new HandlerKey(URL, POST))
                .hasSameHashCodeAs(new HandlerKey(URL, POST));
    }

    @DisplayName("url이 같고 requestMethod가 다르면 equals&hashCode는 다르다.")
    @Test
    void equalsAndHashCodeFailureWhenOnlyRequestMethodNotSame() {
        // given
        // when
        // then
        assertThat(new HandlerKey(URL, POST))
                .isNotEqualTo(new HandlerKey(URL, GET))
                .doesNotHaveSameHashCodeAs(new HandlerKey(URL, GET));
    }

    @DisplayName("url이 다르고 requestMethod이 같으면 equals&hashCode는 다르다.")
    @Test
    void equalsAndHashCodeFailureWhenOnlyUrlNotSame() {
        // given
        // when
        // then
        assertThat(new HandlerKey(URL, POST))
                .isNotEqualTo(new HandlerKey(URL + 1, POST))
                .doesNotHaveSameHashCodeAs(new HandlerKey(URL + 1, POST));
    }
}