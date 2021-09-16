package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.exception.NoSuchRequestMappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class DispatcherServletTest {

    DispatcherServlet dispatcherServlet;
    HttpServletRequest request;
    HttpServletResponse response;
    HandlerMapping handlerMapping;
    HandlerAdapter handlerAdapter;
    RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        handlerMapping = new AnnotationHandlerMapping("samples");
        dispatcherServlet.addHandlerMapping(handlerMapping);
        handlerAdapter = new RequestMappingHandlerAdapter();
        dispatcherServlet.addHandlerAdapters(handlerAdapter);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
    }

    @DisplayName("DispatcherServlet의 내부 설정을 초기화한다.")
    @Test
    void init() {
        assertDoesNotThrow(() -> dispatcherServlet.init());
    }

    @DisplayName("매핑된 URI로 요청을 받으면 적절한 뷰를 반환한다.")
    @Test
    void service() throws ServletException, IOException {
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @DisplayName("잘못된 URI로 요청 시 NoSuchRequestMappingException이 발생한다.")
    @Test
    void invalidURI() throws ServletException, IOException {
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/InvalidPath");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.init();
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(NoSuchRequestMappingException.class)
                .hasMessage("No such request mapping");

        verify(requestDispatcher, times(0)).forward(request, response);
    }
}