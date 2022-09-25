package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.support.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ControllerHandlerAdapterTest {

    @Test
    void Handler가_Controller이면_ControllerHandlerAdapter는_support한다() {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final boolean expected = true;

        // when
        final boolean actual = handlerAdapter.supports((Controller) (req, res) -> null);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void Handler를_동작시킨다() {
        // given
        final HandlerAdapter handlerAdapter = new ControllerHandlerAdapter();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        // when
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, (Controller) (req, res) -> "viewName");

        // then
        assertAll(
                () -> assertThat(modelAndView.getModel()).isEmpty(),
                () -> assertThat(modelAndView.getView()).usingRecursiveComparison()
                        .isEqualTo(new JspView("viewName"))
        );
    }
}
