package nextstep.mvc.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestImplementController;

class HandlerAdapterRepositoryTest {

    @DisplayName("handler에 알맞은 adapter를 찾는다.")
    @Test
    void getAdapter() {
        final HandlerAdapterRepository handlerAdapterRepository = new HandlerAdapterRepository();
        handlerAdapterRepository.init();

        final TestImplementController handler = new TestImplementController();
        assertThat(handlerAdapterRepository.getAdapter(handler)).isInstanceOf(ControllerAdapter.class);
    }
}
