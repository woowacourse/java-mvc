package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ManualHandlerAdapterTest {

    @DisplayName("핸들러가 Controller 타입이면 true를 반환한다.")
    @Test
    void supports() {
        // given
        final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        final Controller controller = mock(Controller.class);

        // when
        final boolean actual = manualHandlerAdapter.supports(controller);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("핸들러의 handle 메서드를 실행시킨다.")
    @Test
    void handle() throws Exception {
        // given
        final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();
        final Controller controller = mock(Controller.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        given(controller.execute(request, response))
                .willReturn("path");

        // when
        final ModelAndView modelAndView = manualHandlerAdapter.handle(request, response, controller);

        // then
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
