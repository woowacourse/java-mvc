package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

import static org.assertj.core.api.Assertions.assertThat;

class AnnotaionHandlerAdapterTest {

    private final AnnotaionHandlerAdapter annotaionHandlerAdapter = new AnnotaionHandlerAdapter();

    @Test
    @DisplayName("지원하는 handler 타입이면 true를 반환한다.")
    void supports() {
        //given
        HandlerExecution handlerExecution = new HandlerExecution(null, null);

        //when
        final boolean supports = annotaionHandlerAdapter.supports(handlerExecution);

        //then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("지원하지 않는 handler 타입이면 false를 반환한다.")
    void supports_fail() {
        //given
        Controller controller = new ForwardController("");

        //when
        final boolean supports = annotaionHandlerAdapter.supports(controller);

        //then
        assertThat(supports).isFalse();
    }
}
