package nextstep.mvc.handler;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.controller.HandlerExecution;
import nextstep.mvc.controller.HandlerExecutionAdapter;
import nextstep.mvc.controller.HandlerExecutionAdapter.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdaptersTest {

    @DisplayName("알맞은 어댑터를 선택하는 기능 테스트")
    @Test
    void chooseProperAdapterTest() {
        //given
        HandlerAdapters handlerAdapters = new HandlerAdapters(new Builder().setDefault().build());
        HandlerExecution handlerExecution = new HandlerExecution(null, null);
        //when
        HandlerAdapter handlerAdapter = handlerAdapters.chooseProperAdapter(handlerExecution);
        //then
        assertThat(handlerAdapter).isInstanceOf(HandlerExecutionAdapter.class);
    }
}