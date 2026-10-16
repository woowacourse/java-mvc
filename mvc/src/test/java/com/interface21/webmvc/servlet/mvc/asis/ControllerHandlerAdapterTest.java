package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ControllerHandlerAdapterTest {

    private final ControllerHandlerAdapter adapter = new ControllerHandlerAdapter();

    @Test
    void 기존_컨트롤러만_지원한다() {
        // given
        Class<?> controllerType = ForwardController.class;

        // when
        boolean supportsController = adapter.supports(controllerType);
        boolean supportsExecution = adapter.supports(HandlerExecution.class);

        // then
        assertThat(supportsController).isTrue();
        assertThat(supportsExecution).isFalse();
    }

    @Test
    void 기존_컨트롤러의_뷰_이름을_ModelAndView로_변환한다() throws Exception {
        // given
        Controller controller = new ForwardController("/index.jsp");
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        // when
        ModelAndView modelAndView = adapter.handle(controller, request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        // then
        verify(requestDispatcher).forward(request, response);
        assertThat(modelAndView.getModel()).isEmpty();
    }
}
