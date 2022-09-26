package nextstep.mvc.controller.tobe.adapters;

import static nextstep.fixtures.HttpServletFixtures.createRequest;
import static nextstep.fixtures.HttpServletFixtures.createResponse;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;
import samples.ManualController;

class ManualHandlerAdapterTest {

    @Test
    void supports() {
        // given
        ManualController controller = new ManualController();
        ManualHandlerAdapter adapter = new ManualHandlerAdapter();
        // when
        boolean isSupports = adapter.supports(controller);
        // then
        assertThat(isSupports).isTrue();
    }

    @Test
    void handle() throws Exception {
        // given
        ManualController controller = new ManualController();
        ManualHandlerAdapter adapter = new ManualHandlerAdapter();
        // when
        Object result = adapter.handle(createRequest(), createResponse(), controller);
        // then
        assertThat(result).isNotNull();
    }
}
