package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.techcourse.controller.legacy.LoginViewController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("어노테이션 기반 컨트롤러를 찾아서 처리한다.")
    @Test
    void processAnnotationBasedController() throws ServletException {
        // given
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/register");

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(argumentCaptor.capture()))
                .thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(argumentCaptor.getValue())
                .isEqualTo("/register.jsp");
    }

    @DisplayName("Controller 인터페이스 기반 컨트롤러를 찾아서 처리한다.")
    @Test
    void processInterfaceBasedController() throws ServletException {
        // given
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        registerFakeHandlerMapping(new LoginViewController());

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(argumentCaptor.capture()))
                .thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(argumentCaptor.getValue())
                .isEqualTo("/login.jsp");
    }

    @DisplayName("요청에 해당하는 핸들러를 찾을 수 없으면 예외가 발생한다.")
    @Test
    void throwsWhenHandlerNotFound() {
        // given
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/not-found");

        // when & then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("요청에 해당하는 핸들러를 찾을 수 없습니다.");
    }

    @DisplayName("요청에 해당하는 핸들러 어댑터를 찾을 수 없으면 예외가 발생한다.")
    @Test
    void throwsWhenHandlerAdapterNotFound() {
        // given
        registerFakeHandlerMapping("test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");

        // when & then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("요청에 해당하는 핸들러 어댑터를 찾을 수 없습니다.");
    }

    private void registerFakeHandlerMapping(Object retVal) {
        dispatcherServlet.addHandlerMapping(new HandlerMapping() {
            @Override
            public void initialize() {
            }

            @Override
            public Object getHandler(HttpServletRequest request) {
                return retVal;
            }
        });
    }
}
