package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualController;

class ManualHandlerAdapterTest {

    private final ManualHandlerAdapter adapter = new ManualHandlerAdapter();

    @Test
    @DisplayName("어댑터가 Controller를 구현한 핸들러인 경우 지원한다..")
    void supports() {
        // given
        final HandlerExecution notSupportedHandler = new HandlerExecution(null, null);
        final Controller supportedHandler = new TestManualController();

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
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final Controller handler = new TestManualController();

        // when
        final ModelAndView expect = adapter.handle(request, response, handler);

        // then
        assertThat(expect.getView()).isNotNull();
    }
}
