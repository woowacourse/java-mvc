package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    private final ManualHandlerAdapter adapter = new ManualHandlerAdapter();

    @Test
    @DisplayName("어댑터가 Controller를 구현한 핸들러인 경우 지원한다..")
    void supports() {
        // given
        final HandlerExecution notSupportedHandler = new HandlerExecution(null, null);
        final Controller supportedHandler = new ExampleController();

        // when
        final boolean notSupportExpected = adapter.supports(notSupportedHandler);
        final boolean supportExpected = adapter.supports(supportedHandler);

        // then
        assertAll(
                () -> assertThat(notSupportExpected).isFalse(),
                () -> assertThat(supportExpected).isTrue()
        );
    }

    @Test
    @DisplayName("어댑터가 핸들러를 실행한다.")
    void handle() throws Exception {
        // given
        final ExampleController handler = new ExampleController();

        // when
        final ModelAndView expect = adapter.handle(null, null, handler);

        // then
        assertThat(expect.getView()).isNotNull();
    }

    private class ExampleController implements Controller {

        @Override
        public String execute(final HttpServletRequest req, final HttpServletResponse res) {
            return "Example";
        }
    }
}
