package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.registry.HandlerAdapterRegistry;
import nextstep.mvc.registry.HandlerMappingRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
//
//    private HandlerMappingRegistry handlerMappingRegistry;
//    private HandlerAdapterRegistry handlerAdapterRegistry;

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        handlerMapping = mock(HandlerMapping.class);
        handlerAdapter = mock(HandlerAdapter.class);

//        handlerMappingRegistry = mock(HandlerMappingRegistry.class);
//        handlerAdapterRegistry = mock(HandlerAdapterRegistry.class);

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addToHandlerAdapterRegistry(handlerAdapter);
        dispatcherServlet.addToHandlerMappingRegistry(handlerMapping);

        dispatcherServlet.init();
    }

    @DisplayName("핸들러 매핑에서 핸들러 찾기 - 적절한 핸들러가 없을 시 예외를 던진다.")
    @Test
    void findHandlerWhenNotExist() {
        given(handlerMapping.getHandler(request)).willReturn(null);
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("요청에 해당하는 Handler를 찾을 수 없습니다.");
    }

    @DisplayName("적절한 핸들러 Adapter 찾기 - 적절한 핸들러 어댑터가 없을 시 예외를 던진다.")
    @Test
    void findHandlerAdapterWhenNotExist() {
        Object handler = new Object();
        given(handlerMapping.getHandler(request)).willReturn(handler);
        given(handlerAdapter.supports(handler)).willReturn(Boolean.FALSE);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessageContaining("Handler를 지원하는 HandlerAdapter를 찾을 수 없습니다.");
    }
}
