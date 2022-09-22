package nextstep.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerAdapterTest {
    @DisplayName(value = "지원하는 handler면 true")
    @Test
    void AnnotationHandlerAdapterTrue() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final Object handler = mock(HandlerExecution.class);

        // when & then
        assertThat(handlerAdapter.supports(handler)).isTrue();
    }

    @DisplayName(value = "지원하지 않는 handler면 false")
    @Test
    void AnnotationHandlerAdapterFalse() {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();
        final Object handler = new Object();

        // when & then
        assertThat(handlerAdapter.supports(handler)).isFalse();
    }

    @DisplayName(value = "handle() 호출 시 HandlerExecution의 handle()이 호출됨")
    @Test
    void callHandle() throws Exception {
        // given
        final HandlerAdapter handlerAdapter = new AnnotationHandlerAdapter();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when
        handlerAdapter.handle(request, response, handlerExecution);

        // then
        verify(handlerExecution).handle(request, response);
    }
}
