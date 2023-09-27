package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import web.org.springframework.web.bind.annotation.RequestMethod;

class HandlerKeyTest {

    @DisplayName("RequestURI와 Method 가 동일하다면 equals가 true이다.")
    @Test
    void testEquals() {
        // given
        final HandlerKey handlerKey = new HandlerKey("/hello", RequestMethod.GET);
        final HandlerKey handlerKey2 = new HandlerKey("/hello", RequestMethod.GET);

        // when
        final boolean isEqual = handlerKey.equals(handlerKey2);

        // then
        assertThat(isEqual).isTrue();
    }

    @DisplayName("RequestURI와 Method 중 하나가 다르다면 equals가 false이다.")
    @Test
    void testEquals_false() {
        // given
        final HandlerKey handlerKey = new HandlerKey("/hello", RequestMethod.GET);
        final HandlerKey handlerKey2 = new HandlerKey("/hello", RequestMethod.POST);

        // when
        final boolean isEqual = handlerKey.equals(handlerKey2);

        // then
        assertThat(isEqual).isFalse();
    }
}
