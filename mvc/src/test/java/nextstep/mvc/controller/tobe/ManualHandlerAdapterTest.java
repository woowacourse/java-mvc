package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.controller.tobe.adapter.ManualHandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class ManualHandlerAdapterTest {
    private final HandlerAdapter handlerAdapter = new ManualHandlerAdapter();

    @Test
    void supports() {
        final Controller controller = new ForwardController("");
        assertThat(handlerAdapter.supports(controller)).isTrue();
    }

    @Test
    void handle() throws Exception {
        //given
        final HttpServletRequest request = spy(HttpServletRequest.class);
        final HttpServletResponse response = spy(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("");
        when(request.getMethod()).thenReturn("GET");
        final Controller controller = new ForwardController("");

        //when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, controller);

        //then
        JspView jspView = (JspView) modelAndView.getView();
        assertThat(jspView.getViewName()).isEqualTo("");
    }
}
