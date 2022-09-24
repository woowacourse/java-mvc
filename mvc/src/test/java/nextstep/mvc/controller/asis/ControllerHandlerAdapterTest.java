package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    @Test
    void supports_메서드는_주어진_핸들러가_Controller_인지_확인한다() {
        // given
        final Controller controller = mock(Controller.class);
        final HandlerExecution execution = mock(HandlerExecution.class);

        final HandlerAdapter adapter = new ControllerHandlerAdapter();

        // when & then
        assertAll(
                () -> assertThat(adapter.supports(controller)).isTrue(),
                () -> assertThat(adapter.supports(execution)).isFalse()
        );
    }

    @Test
    void handle_메서드는_핸들러_작업을_수행하고_ModelAndView_를_반환한다() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);

        when(controller.execute(request, response)).thenReturn("test");

        // when
        final HandlerAdapter adapter = new ControllerHandlerAdapter();
        final ModelAndView actual = adapter.handle(request, response, controller);

        // then
        assertAll(
                () -> verify(controller).execute(request, response),
                () -> assertThat(actual.getView()).isInstanceOf(JspView.class)
        );
    }
}
