package nextstep.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    @DisplayName(value = "지원하는 handler면 true")
    @Test
    void ControllerHandlerAdapterTrue() {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final Object handler = mock(Controller.class);

        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @DisplayName(value = "지원하지 않는 handler면 false")
    @Test
    void ControllerHandlerAdapterFalse() {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final Object handler = new Object();

        // when & then
        assertThat(handlerAdapter.supports(handler)).isFalse();
    }

    @DisplayName(value = "handle() 호출 시 Controller의 execute()가 호출됨")
    @Test
    void callHandle() throws Exception {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);

        // when
        handlerAdapter.handle(request, response, controller);

        // then
        verify(controller).execute(request, response);
    }
}
