package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

class HandlerExecutionAdapterTest {

    @DisplayName("HandlerExecutionAdapter 는 handlerExecution 을 지원한다.")
    @Test
    void supports() {
        // given
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when, then
        HandlerExecutionAdapter handlerAdapter = new HandlerExecutionAdapter();
        assertThat(handlerAdapter.supports(handlerExecution)).isTrue();
    }

    @DisplayName("HandlerExecutionAdapter 로 handlerExecution 를 실행한다.")
    @Test
    void handle() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handlerExecution = mock(HandlerExecution.class);

        HandlerExecutionAdapter handlerAdapter = new HandlerExecutionAdapter();

        // when
        handlerAdapter.handle(request, response, handlerExecution);

        // then
        then(handlerExecution).should(times(1))
                .handle(request, response);
    }
}
