package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;

class ControllerHandlerAdapterTest {

    @Test
    void Controller_타입일_경우_ture를_반환한다() {
        // given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        final Controller controller = mock(Controller.class);

        // expect
        final boolean actual = controllerHandlerAdapter.support(controller);

        assertThat(actual).isTrue();
    }

    @Test
    void Controller_타입이_아닐_경우_false를_반환한다() {
        // given
        final ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        final context.org.springframework.stereotype.Controller controller = mock(context.org.springframework.stereotype.Controller.class);

        // expect
        final boolean actual = controllerHandlerAdapter.support(controller);

        assertThat(actual).isFalse();
    }

    @Test
    void 로직을_수행하고_모델뷰를_반환한다() throws Exception {
        // given
        final ControllerHandlerAdapter controllerHandlerAdapter = mock(ControllerHandlerAdapter.class);

        // when
        final ModelAndView expected = mock(ModelAndView.class);

        when(controllerHandlerAdapter.doInternalService(
                any(HttpServletRequest.class), any(HttpServletResponse.class), any()
        )).thenReturn(expected);

        // then
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);

        final ModelAndView actual = controllerHandlerAdapter.doInternalService(request, response, controller);

        assertThat(actual).isEqualTo(expected);
    }
}
