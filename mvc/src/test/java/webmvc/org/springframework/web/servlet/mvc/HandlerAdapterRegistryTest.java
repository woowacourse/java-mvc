package webmvc.org.springframework.web.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.util.NoSuchElementException;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("ModelAndView 를 반환하는 형태의 Handler 를 적용한다.")
    void adaptHandler_handlerExecution() throws Exception {
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        final var handler = mock(HandlerExecution.class);
        final var view = new JspView("redirect:/test.jsp");
        when(handler.handle(request, response)).thenReturn(new ModelAndView(view));

        handlerAdapterRegistry.adaptHandler(request, response, handler);

        verify(handler).handle(request, response);
    }

    @Test
    @DisplayName("View name 을 반환하는 형태의 Handler 를 적용한다.")
    void adaptHandler_controller() throws Exception {
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        final var handler = new ForwardController("redirect:/test.jsp");

        handlerAdapterRegistry.adaptHandler(request, response, handler);

        verify(response).sendRedirect("/test.jsp");
    }

    @Test
    @DisplayName("일치하지 않는 형태의 Handler 를 적용하려고 하면 예외가 발생한다.")
    void adaptHandler_validate() {
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        assertThatThrownBy((() -> handlerAdapterRegistry.adaptHandler(request, response, new ForwardController("/"))))
                .isInstanceOf(NoSuchElementException.class);
    }

}
