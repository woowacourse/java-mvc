package webmvc.org.springframework.web.servlet.mvc.tobe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
