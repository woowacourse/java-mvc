package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ControllerHandlerAdaptorTest {

    @Test
    void ManualHandlerAdaptor는_Controller_인터페이스를_상속받은_클래스의_객체를_처리할_수_있다() {
        // given
        final ForwardController forwardController = new ForwardController("/123.html");

        final ControllerHandlerAdaptor controllerHandlerAdaptor = new ControllerHandlerAdaptor();

        // when
        final boolean result = controllerHandlerAdaptor.supports(forwardController);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void ManualHandlerAdaptor는_Controller_인터페이스를_상속받은_클래스의_객체가_아니면_처리할_수_없다() {
        // given
        final TestController testController = new TestController();

        final ControllerHandlerAdaptor controllerHandlerAdaptor = new ControllerHandlerAdaptor();

        // when
        final boolean result = controllerHandlerAdaptor.supports(testController);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void Handler를_처리하면_modelAndView를_반환한다() throws Exception {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdaptor();

        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        final ModelAndView result = handlerAdapter.handle(request, response, new ForwardController("/123.html"));

        // then
        AssertionsForClassTypes.assertThat(result).extracting("view", "model")
                .containsExactly(new JspView("/123.html"), Map.of());
    }
}
