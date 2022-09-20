package nextstep.mvc;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.ServletException;
import nextstep.mvc.handlerAdapter.AnnotationHandlerAdapter;
import nextstep.mvc.handlerMapping.AnnotationHandlerMapping;

class DispatcherServletTest {

    @Test
    @DisplayName("어노테이션 기반 service를 검증한다")
    void annotationService() {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/get-test");
        when(httpServletRequest.getAttribute("id")).thenReturn("1");
        when(httpServletRequest.getRequestDispatcher("")).thenReturn(requestDispatcher);

        dispatcherServlet.service(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getRequestDispatcher("");
    }

    @Test
    @DisplayName("handlerAdapter가 존재하지 않을 경우 에러를 발생시킨다.")
    void handlerAdapterError() {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/get-test");

        assertThatThrownBy(() -> dispatcherServlet.service(httpServletRequest, httpServletResponse))
            .isInstanceOf(ServletException.class)
            .hasMessage("Handler Adapter를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("handlerMapping이 존재하지 않을 경우 에러를 발생시킨다.")
    void handlerMappingError() {
        // given
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.init();

        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        when(httpServletRequest.getMethod()).thenReturn("GET");
        when(httpServletRequest.getRequestURI()).thenReturn("/get-test");

        assertThatThrownBy(() -> dispatcherServlet.service(httpServletRequest, httpServletResponse))
            .isInstanceOf(ServletException.class)
            .hasMessage("handler를 찾을 수 없습니다.");
    }
}