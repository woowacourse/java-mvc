package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class DispatcherServletTest {


    @DisplayName("handler가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void dispatcherServletExceptionIfNotExistHandler() {
        //given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var handlerMapping = mock(HandlerMapping.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        //when
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping);

        //then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("처리할 수 있는 handler를 찾지 못했습니다.");
    }

    @DisplayName("handler adapter가 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void dispatcherServletExceptionIfNotExistHandlerAdapter() {
        //given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        var handlerMapping = mock(HandlerMapping.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        //when
        when(handlerMapping.getHandler(request)).thenReturn(new TestController());
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping);

        //then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("처리할 수 있는 handlerAdapter를 찾지 못했습니다.");
    }
}