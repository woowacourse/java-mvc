package webmvc.org.springframework.web.servlet.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterException;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerExecution;

class HandlerAdaptersTest {

    @DisplayName("Handler 객체가 HandlerExecution 이라면 어노테이션 기반의 adapter 가 반환된다.")
    @Test
    void getHandlerAdapter() throws Exception {
        // given
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        final HandlerExecution handler = Mockito.mock(HandlerExecution.class);

        // when
        final HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

        // then
        assertThat(handlerAdapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @DisplayName("지원되는 HandlerAdapter가 없다면 예외가 발생한다.")
    @Test
    void getHandlerAdapter_nothingSupported() throws Exception {
        // given
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        final Object handler = new Object();

        // when, then
        assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(handler))
            .isInstanceOf(HandlerAdapterException.NotFoundException.class);
    }
}
