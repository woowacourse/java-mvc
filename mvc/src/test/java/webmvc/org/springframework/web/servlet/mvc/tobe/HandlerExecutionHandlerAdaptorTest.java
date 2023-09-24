package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

class HandlerExecutionHandlerAdaptorTest {

    private final HandlerExecutionHandlerAdaptor handlerAdaptor = new HandlerExecutionHandlerAdaptor();

    @Test
    void Controller_어노테이션을_적용한_컨트롤러이면_지원한다() {
        // given
        Object handler = new TestController();

        // when
        boolean result = handlerAdaptor.support(handler);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void Controller_인터페이스를_구현한_컨트롤러이면_지원한다() {
        // given
        Object handler = (Controller) (request, response) -> "Test Controller";

        // when
        boolean result = handlerAdaptor.support(handler);

        // then
        assertThat(result).isFalse();
    }
}
