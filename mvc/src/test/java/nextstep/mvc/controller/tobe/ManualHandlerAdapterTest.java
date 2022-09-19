package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {

    private ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

    @Nested
    class supports_메서드는 {

        @Nested
        class 실행할_수_없는_Handler이면 {

            @Test
            void false를_반환한다() {
                final HandlerExecution handler = mock(HandlerExecution.class);

                assertThat(manualHandlerAdapter.supports(handler)).isFalse();
            }
        }

        @Nested
        class 실행할_수_있는_Handler이면 {

            @Test
            void true를_반환한다() {
                final Controller handler = mock(Controller.class);

                assertThat(manualHandlerAdapter.supports(handler)).isTrue();
            }
        }
    }

    @Nested
    class handle_메서드는 {

        @Nested
        class 실행가능한_handler일_경우 {

            @Test
            void 해당_handler를_실행한다() throws Exception {
                final Controller handler = mock(Controller.class);
                final HttpServletRequest request = mock(HttpServletRequest.class);
                final HttpServletResponse response = mock(HttpServletResponse.class);

                when(handler.execute(request, response)).thenReturn("/index.jsp");

                final ModelAndView actual = manualHandlerAdapter.handle(request, response, handler);

                assertThat(actual.getView()).isNotNull();
            }
        }
    }
}
