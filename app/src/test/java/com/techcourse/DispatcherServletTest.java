package com.techcourse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.tobe.mapper.HandlerMappingRegistry;

class DispatcherServletTest {

    @Test
    @DisplayName("적절한 핸들러 매핑과 어댑터를 찾아 생성한 View를 render한다.")
    void service() throws Exception {
        final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        final HandlerMappingRegistry mockHandlerMappingRegistry = mock(HandlerMappingRegistry.class);
        final HandlerAdapterRegistry mockHandlerAdapterRegistry = mock(HandlerAdapterRegistry.class);
        final HandlerAdapter mockHandlerAdapter = mock(HandlerAdapter.class);
        final View mockView = mock(View.class);
        given(mockHandlerMappingRegistry.getHandler(any()))
                .willReturn(Optional.of(new Object()));
        given(mockHandlerAdapterRegistry.getHandlerAdapter(any()))
                .willReturn(mockHandlerAdapter);
        given(mockHandlerAdapter.handle(any(), any(), any()))
                .willReturn(new ModelAndView(mockView));
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(mockHandlerMappingRegistry,
                mockHandlerAdapterRegistry);

        // when
        dispatcherServlet.service(mockRequest, mockResponse);

        // then
        verify(mockView, times(1)).render(any(), any(), any());
    }
}
