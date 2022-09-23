package nextstep.mvc.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.Controller;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.TestManualController;

@DisplayName("ManualHnalderAdapter의")
class ManualHandlerAdapterTest {

    @Test
    @DisplayName("handler 메서드는 controller로 요청을 처리하고 ModelAndView를 반환한다.")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        final Controller handler = new TestManualController();

        // when
        final ModelAndView actual = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(actual).isNotNull();
    }

    @Nested
    @DisplayName("supports 메서드는")
    class Supports {

        @Test
        @DisplayName("handler가 Controller면 true를 반환한다.")
        void supports_controller_true() {
            // given
            final ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
            final Controller handler = new TestManualController();

            // when
            final boolean actual = handlerAdapter.supports(handler);

            // then
            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("handler가 Controller가 아니면 false를 반환한다.")
        void supports_notController_true() {
            // given
            final ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
            final String handler = "not-controller";

            // when
            final boolean actual = handlerAdapter.supports(handler);

            // then
            assertThat(actual).isFalse();
        }
    }
}
