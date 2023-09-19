package webmvc.org.springframework.web.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ControllerHandlerAdaptorTest {

    private final ControllerHandlerAdaptor handlerAdaptor = new ControllerHandlerAdaptor();

    @Test
    void Controller_인터페이스를_구현한_컨트롤러이면_지원한다() {
        // given
        Object handler = (Controller) (req, res) -> "Test Controller";

        // when
        boolean result = handlerAdaptor.support(handler);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void Controller_인터페이스를_구현한_컨트롤러이면_지원하지_않는다() {
        // given
        Object handler = new HandlerExecution(null);

        // when
        boolean result = handlerAdaptor.support(handler);

        // then
        assertThat(result).isFalse();
    }
}
