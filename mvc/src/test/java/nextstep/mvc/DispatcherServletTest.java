package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import javassist.NotFoundException;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private static final String TEST_VIEW = "testView";

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Controller controller;
    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() throws Exception {
        dispatcherServlet = new DispatcherServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        controller = mock(Controller.class);
        handlerMapping = mock(HandlerMapping.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(controller.execute(request, response)).thenReturn(TEST_VIEW);
        when(request.getRequestDispatcher(TEST_VIEW)).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(request, response);

        dispatcherServlet.addHandlerMapping(handlerMapping);
    }

    @DisplayName("해당 타입을 지원하는 어댑터가 있는 경우 렌더링")
    @Test
    void service() throws Exception {
        when(handlerMapping.getHandler(request)).thenReturn(controller);
        when(request.getRequestURI()).thenReturn("/manual");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.service(request, response);

        verify(controller, times(1)).execute(request, response);
    }

    @DisplayName("핸들러 매핑에서 적절한 핸들러를 찾지 못한 경우 예외 처리")
    @Test
    void notFoundHandler() {
        when(handlerMapping.getHandler(request)).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/manual");
        when(request.getMethod()).thenReturn("GET");

        assertThatExceptionOfType(ServletException.class)
            .isThrownBy(() -> dispatcherServlet.service(request, response))
            .withMessageContaining("적절한 핸들러를 찾지 못 했습니다");
    }

    @DisplayName("해당 타입을 지원하는 어댑터가 없는 경우 예외 처리")
    @Test
    void serviceNotFound() {
        when(handlerMapping.getHandler(request)).thenReturn(new ModelAndView(new JspView("")));
        when(request.getRequestURI()).thenReturn("/not-found");
        when(request.getMethod()).thenReturn("GET");

        assertThatExceptionOfType(ServletException.class)
            .isThrownBy(() -> dispatcherServlet.service(request, response))
            .withMessageContaining("지원하지 않는 핸들러입니다.");
    }
}