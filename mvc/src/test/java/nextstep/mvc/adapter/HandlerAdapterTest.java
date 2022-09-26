package nextstep.mvc.adapter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapterRegistry;
import nextstep.mvc.mapping.HandlerExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.HandlerFixture;

class HandlerAdapterTest {

    private static final HandlerFixture CONTROLLER = new HandlerFixture();

    private HandlerAdapterRegistry handlerAdapterRegistry;
    private ModelAndViewHandlerAdapter modelAndViewHandlerAdapter;
    private ViewNameHandlerAdapter viewNameHandlerAdapter;
    private VoidHandlerAdapter voidHandlerAdapter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        modelAndViewHandlerAdapter = mock(ModelAndViewHandlerAdapter.class);
        handlerAdapterRegistry.addHandlerAdapter(modelAndViewHandlerAdapter);
        viewNameHandlerAdapter = mock(ViewNameHandlerAdapter.class);
        handlerAdapterRegistry.addHandlerAdapter(viewNameHandlerAdapter);
        voidHandlerAdapter = mock(VoidHandlerAdapter.class);
        handlerAdapterRegistry.addHandlerAdapter(voidHandlerAdapter);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("ModelAndView를 반환하는 핸들러는 ModelAndViewHandlerAdapter에 의해 실행된다.")
    @Test
    void modelAndViewHandlerAdapter() throws Exception {
        final var method = CONTROLLER.getClass().getMethod("modelAndViewHandler",
                HttpServletRequest.class, HttpServletResponse.class);
        final var handler = new HandlerExecution(CONTROLLER, method);

        when(modelAndViewHandlerAdapter.supports(handler)).thenCallRealMethod();
        when(modelAndViewHandlerAdapter.handle(request, response, handler)).thenCallRealMethod();

        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        handlerAdapter.handle(request, response, handler);

        verify(modelAndViewHandlerAdapter, times(1)).supports(handler);
        verify(modelAndViewHandlerAdapter, times(1)).handle(request, response, handler);
    }


    @DisplayName("String을 반환하는 핸들러는 ViewNameHandlerAdapter에 의해 실행된다.")
    @Test
    void viewNameHandlerAdapter() throws Exception {
        final var method = CONTROLLER.getClass().getMethod("viewNameHandler",
                HttpServletRequest.class, HttpServletResponse.class);
        final var handler = new HandlerExecution(CONTROLLER, method);

        when(viewNameHandlerAdapter.supports(handler)).thenCallRealMethod();
        when(viewNameHandlerAdapter.handle(request, response, handler)).thenCallRealMethod();

        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        handlerAdapter.handle(request, response, handler);

        verify(viewNameHandlerAdapter, times(1)).supports(handler);
        verify(viewNameHandlerAdapter, times(1)).handle(request, response, handler);
    }

    @DisplayName("반환형이 없는 핸들러는 VoidHandlerAdapter에 의해 실행된다.")
    @Test
    void voidHandlerAdapter() throws Exception {
        final var method = CONTROLLER.getClass().getMethod("voidHandler",
                HttpServletRequest.class, HttpServletResponse.class);
        final var handler = new HandlerExecution(CONTROLLER, method);

        when(voidHandlerAdapter.supports(handler)).thenCallRealMethod();
        when(voidHandlerAdapter.handle(request, response, handler)).thenCallRealMethod();

        final var handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);
        handlerAdapter.handle(request, response, handler);

        verify(voidHandlerAdapter, times(1)).supports(handler);
        verify(voidHandlerAdapter, times(1)).handle(request, response, handler);
    }
}
