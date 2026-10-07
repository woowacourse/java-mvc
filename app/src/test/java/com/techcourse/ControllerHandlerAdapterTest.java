package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ControllerHandlerAdapterTest {

    @Test
    @DisplayName("Controller 타입만 지원한다")
    void supports() {
        final var adapter = new ControllerHandlerAdapter();

        assertThat(adapter.supports(mock(Controller.class))).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
    }

    @Test
    @DisplayName("Controller를 실행하고 반환된 이름으로 View를 생성한다")
    void handle() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        final var controller = mock(Controller.class);
        when(controller.execute(request, response)).thenReturn("/index.jsp");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        final var adapter = new ControllerHandlerAdapter();
        final var mav = adapter.handle(controller, request, response);
        mav.getView().render(mav.getModel(), request, response);

        verify(controller).execute(request, response);
        verify(requestDispatcher).forward(request, response);
    }
}
