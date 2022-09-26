package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class HandlerExecutionAdapterTest {

//    @Test
//    void supports_메서드는_주어진_핸들러가_HandlerExecution_인지_확인한다() {
//        // given
//        final Controller controller = mock(Controller.class);
//        final HandlerExecution execution = mock(HandlerExecution.class);
//
//        final HandlerAdapter adapter = new HandlerExecutionAdapter();
//
//        // when & then
//        assertAll(
//                () -> assertThat(adapter.supports(execution)).isTrue(),
//                () -> assertThat(adapter.supports(controller)).isFalse()
//        );
//    }

    @Test
    void handle_메서드는_핸들러_작업을_수행하고_ModelAndView_를_반환한다() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final HandlerExecution execution = mock(HandlerExecution.class);

        final ModelAndView testModelAndView = new ModelAndView(new JspView("test"));

        when(execution.handle(request, response)).thenReturn(testModelAndView);

        // when
        final HandlerAdapter adapter = new HandlerExecutionAdapter();
        final ModelAndView actual = adapter.handle(request, response, execution);

        // then
        assertAll(
                () -> verify(execution).handle(request, response),
                () -> assertThat(actual).isSameAs(testModelAndView)
        );
    }
}
