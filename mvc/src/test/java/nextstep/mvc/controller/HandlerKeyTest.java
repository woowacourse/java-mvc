package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerKeyTest {

    @DisplayName("URL과 RequestMethod가 동일한 HandlerKey는 동등하다")
    @Test
    void handlerKeys_are_equals_based_on_url_and_requestMethod() {
        // given
        final var url = new String("/login");
        final var requestMethod = RequestMethod.GET;
        final var url2 = new String("/login");
        final var requestMethod2 = RequestMethod.GET;

        // when
        final var handlerKey = new HandlerKey(url, requestMethod);
        final var handlerKey2 = new HandlerKey(url2, requestMethod2);

        // then
        assertThat(handlerKey).isEqualTo(handlerKey2);
    }

    @DisplayName("URL과 RequestMethod 가운데 하나라도 다르면 HandlerKey는 동등하지 않다")
    @Test
    void handlerKeys_are_different_if_one_of_url_or_requestMethod_is_different() {
        // given
        final var url = new String("/login");
        final var requestMethod = RequestMethod.GET;
        final var url2 = new String("/login2");
        final var requestMethod2 = RequestMethod.GET;
        final var url3 = new String("/login");
        final var requestMethod3 = RequestMethod.POST;
        final var url4 = new String("/login2");
        final var requestMethod4 = RequestMethod.POST;

        // when
        final var handlerKey = new HandlerKey(url, requestMethod);
        final var handlerKey2 = new HandlerKey(url2, requestMethod2);
        final var handlerKey3 = new HandlerKey(url3, requestMethod3);
        final var handlerKey4 = new HandlerKey(url4, requestMethod4);

        // then
        assertAll(
                () -> assertThat(handlerKey).isNotEqualTo(handlerKey2),
                () -> assertThat(handlerKey).isNotEqualTo(handlerKey3),
                () -> assertThat(handlerKey).isNotEqualTo(handlerKey4)
        );
    }
}
