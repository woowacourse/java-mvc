package com.techcourse.support.web.handler.adaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

class ManualHandlerAdaptorTest {

    private static final HandlerAdaptor handlerAdaptor = new ManualHandlerAdaptor();

    @Test
    void testSupportsSuccess() {
        //given
        final Controller controller = mock(Controller.class);

        //when
        final boolean result = handlerAdaptor.supports(controller);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void testSupportsFailWhenHandlerIsNotController() {
        //given
        final Object obj = new Object();

        //when
        final boolean result = handlerAdaptor.supports(obj);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void testExecuteSuccess() throws Exception {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Controller controller = mock(Controller.class);

        final String expected = "test.jsp";
        when(controller.execute(request, response))
                .thenReturn(expected);

        //when
        final ModelAndView result = handlerAdaptor.execute(request, response, controller);

        //then
        assertThat(result.getView()).isInstanceOf(JspView.class);
    }
}
