package com.interface21.webmvc.servlet.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("samples");
        dispatcherServlet.init();
    }

    @Test
    @DisplayName("컨트롤러가 반환한 ModelAndView의 모델을 request attribute로 전달한 후 JSP로 forward한다")
    void modelAndViewExposesModelAndForwardsToJsp() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/annotation-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/annotation-test.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        final var inOrder = inOrder(request, requestDispatcher);
        inOrder.verify(request).setAttribute("name", "테리");
        inOrder.verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("컨트롤러로 등록된 적 없는 요청이 들어올 시 404 오류 응답을 보낸다")
    void return404WhenPathNotRegisteredWithController() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/doesnt-exist");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendError(404);
    }
}
