package webmvc.org.springframework.web.servlet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerAdaptersTest {

    @Test
    void HandlerAdapter를_입력받아_추가한다() {
        // given
        final HandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        final HandlerAdapters handlerAdapters = new HandlerAdapters();
        final HandlerExecution handler = new HandlerExecution(null, null);

        // when
        handlerAdapters.add(handlerExecutionHandlerAdapter);

        // then
        assertThat(handlerAdapters.getHandlerAdapter(handler)).isEqualTo(handlerExecutionHandlerAdapter);
    }

    @Test
    void 핸들러를_처리할_수_있는_HandlerAdapter를_반환한다() {
        // given
        final HandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();
        final HandlerAdapters handlerAdapters = new HandlerAdapters(List.of(handlerExecutionHandlerAdapter));
        final HandlerExecution handler = new HandlerExecution(null, null);

        // when
        final HandlerAdapter result = handlerAdapters.getHandlerAdapter(handler);

        // then
        assertThat(result).isEqualTo(handlerExecutionHandlerAdapter);
    }
}
