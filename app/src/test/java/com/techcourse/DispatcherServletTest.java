package com.techcourse;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.handleradapter.ControllerHandlerAdapter;
import com.techcourse.handleradapter.HandlerExecutionHandlerAdapter;
import com.techcourse.handlermapping.ManualHandlerMapping;
import com.techcourse.viewresolver.SimpleViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = getDispatcherServlet();
    }

    private DispatcherServlet getDispatcherServlet() {
        HandlerRegistry handlerRegistry = getHandlerRegistry();
        return new DispatcherServlet(handlerRegistry, new SimpleViewResolver());
    }

    private HandlerRegistry getHandlerRegistry() {
        HandlerMappingRegistry handlerMappingRegistry = getHandlerMappingRegistry();
        HandlerAdapterRegistry handlerAdapterRegistry = getHandlerAdapterRegistry();
        return new HandlerRegistry(handlerMappingRegistry, handlerAdapterRegistry);
    }

    private HandlerMappingRegistry getHandlerMappingRegistry() {
        return new HandlerMappingRegistry(
                List.of(new AnnotationHandlerMapping("com.techcourse.controller"), new ManualHandlerMapping())
        );
    }

    private HandlerAdapterRegistry getHandlerAdapterRegistry() {
        return new HandlerAdapterRegistry(
                List.of(new HandlerExecutionHandlerAdapter(), new ControllerHandlerAdapter())
        );
    }

    @DisplayName("올바른 uri 요청을 보내면 요청을 처리한다.")
    @Test
    void serviceSuccessWithValidRequest() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(request.getRequestURI()).willReturn("/register");
        given(request.getMethod()).willReturn("GET");

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/register.jsp");
    }

    @DisplayName("올바르지 않은 uri 요청을 보내면 에러를 던진다.")
    @Test
    void serviceFailWithInvalidRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(request.getRequestURI()).willReturn("/wrong");
        given(request.getMethod()).willReturn("GET");

        assertThatCode(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }
}
