package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JsonView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class HandlerExecutionHandlerAdapterTest {

    private final HandlerExecutionHandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();

    @DisplayName("HandlerExecution 클래스를 구현한 handler는 true를 반환한다.")
    @Test
    void supports() {
        // given
        HandlerExecution handler = mock(HandlerExecution.class);

        // when
        boolean actual = handlerExecutionHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("HandlerExecution 클래스를 구현하지 않은 handler는 false를 반환한다.")
    @Test
    void supports_false() {
        // given
        Object objectHandler = mock(Object.class);

        // when
        boolean actual = handlerExecutionHandlerAdapter.supports(objectHandler);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("handler를 실행하고 ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handler = mock(HandlerExecution.class);

        given(handler.handle(request, response)).willReturn(new ModelAndView(new JsonView()));

        // when & then
        assertThat(handlerExecutionHandlerAdapter.handle(request, response, handler)).isInstanceOf(ModelAndView.class);
    }
}
