package webmvc.org.springframework.web.servlet.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ForwardController;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

class HandlerExecutorTest {

    private static HandlerExecutor handlerExecutor;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeAll
    static void setUpBeforeAll() {
        final var handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("ModelAndView 를 반환하는 형태의 Handler 를 적용한다.")
    void execute_handlerExecution_JspView() throws Exception {
        final var handler = mock(HandlerExecution.class);
        final var view = new JspView("redirect:/test.jsp");
        when(handler.handle(request, response)).thenReturn(new ModelAndView(view));

        handlerExecutor.execute(request, response, handler);

        verify(handler).handle(request, response);
    }

    @Test
    @DisplayName("View name 을 반환하는 형태의 Handler 를 적용한다.")
    void execute_controller() throws Exception {
        final var handler = new ForwardController("redirect:/test.jsp");

        handlerExecutor.execute(request, response, handler);

        verify(response).sendRedirect("/test.jsp");
    }

}
