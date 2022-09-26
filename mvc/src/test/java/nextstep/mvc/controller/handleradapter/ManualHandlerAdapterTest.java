package nextstep.mvc.controller.handleradapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ManualHandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;
import samples.TestManualController;

@DisplayName("ManualHandlerAdapter 클래스의")
class ManualHandlerAdapterTest {

    @Nested
    @DisplayName("supports 메서드는")
    class Supports {

        @Test
        @DisplayName("handler가 Controller의 구현체라면 true를 반환한다.")
        void success() {
            //given
            final ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
            final Controller handler = new TestManualController();

            //when
            final boolean actual = handlerAdapter.supports(handler);

            //then
            assertThat(actual).isTrue();
        }

        @Test
        @DisplayName("handler가 Controller의 구현체가 아니면 false를 반환한다.")
        void fail() {
            //given
            final ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
            final TestAnnotationController handler = new TestAnnotationController();

            //when
            final boolean actual = handlerAdapter.supports(handler);

            //then
            assertThat(actual).isFalse();
        }
    }

    @Test
    @DisplayName("handle 메서드는 Controller를 execute하여 ModelAndView를 반환한다.")
    void handle() throws Exception {
        //given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        final ManualHandlerAdapter handlerAdapter = new ManualHandlerAdapter();
        final Controller handler = mock(TestManualController.class);

        final String viewName = "test";
        final ModelAndView expected = new ModelAndView(new JspView(viewName));
        given(handler.execute(request, response))
                .willReturn(viewName);

        //when
        final ModelAndView actual = handlerAdapter.handle(request, response, handler);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
