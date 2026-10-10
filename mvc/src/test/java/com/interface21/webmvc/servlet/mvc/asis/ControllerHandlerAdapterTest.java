package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ControllerHandlerAdapterTest {

    private final HandlerAdapter adapter = new ControllerHandlerAdapter();

    @Test
    @DisplayName("레거시 Controller 타입만 지원한다")
    void supportsOnlyLegacyControllers() {
        assertThat(adapter.supports(mock(Controller.class))).isTrue();
        assertThat(adapter.supports(mock(HandlerExecution.class))).isFalse();
        assertThat(adapter.supports(null)).isFalse();
    }

    @Test
    @DisplayName("컨트롤러의 뷰 이름을 렌더링 가능한 ModelAndView로 변환한다")
    void convertsControllerViewNameToRenderableModelAndView() throws Exception {
        final var controller = mock(Controller.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(controller.execute(request, response)).thenReturn("/test.jsp");
        when(request.getRequestDispatcher("/test.jsp")).thenReturn(dispatcher);

        final var result = adapter.handle(controller, request, response);

        verify(controller).execute(request, response);
        assertThat(result.getModel()).isEmpty();
        verifyNoInteractions(dispatcher);
        result.getView().render(result.getModel(), request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("컨트롤러가 반환한 리다이렉트 경로를 유지한다")
    void preservesRedirectViewName() throws Exception {
        final var controller = mock(Controller.class);
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(controller.execute(request, response)).thenReturn("redirect:/users");

        final var result = adapter.handle(controller, request, response);

        verifyNoInteractions(response);
        result.getView().render(result.getModel(), request, response);
        verify(response).sendRedirect("/users");
    }
}
