package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    @Test
    void isSupported() {
        // given
        Controller controller = new ForwardController("/index.jsp");
        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        // when
        boolean actual = controllerHandlerAdapter.isSupported(controller);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 컨트롤러의_반환값을_jsp뷰로_변환한다() throws Exception {
        // given
        Controller controller = mock(Controller.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(controller.execute(request, response)).thenReturn("/index.jsp");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        // when
        ModelAndView result = controllerHandlerAdapter.adapt(request, response, controller);
        result.getView().render(result.getModel(), request, response);

        // then
        verify(controller).execute(request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 리다이렉트_반환값을_jsp뷰로_변환한다() throws Exception {
        // given
        Controller controller = mock(Controller.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(controller.execute(request, response)).thenReturn("redirect:/login");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        ControllerHandlerAdapter controllerHandlerAdapter = new ControllerHandlerAdapter();

        // when
        ModelAndView result = controllerHandlerAdapter.adapt(request, response, controller);
        result.getView().render(result.getModel(), request, response);

        // then
        verify(response).sendRedirect("/login");
    }
}