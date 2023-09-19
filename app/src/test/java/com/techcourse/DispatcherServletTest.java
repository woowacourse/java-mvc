package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappingRegistry;

class DispatcherServletTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    HandlerAdapterRegistry mockHandlerAdapterRegistry;

    @Mock
    HandlerMappingRegistry mockHandlerMappingRegistry;

    @Captor
    ArgumentCaptor<String> urlCaptor;

    DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setting() {
        MockitoAnnotations.openMocks(this);
        dispatcherServlet = new DispatcherServlet(mockHandlerMappingRegistry, mockHandlerAdapterRegistry);
    }

    @Test
    void noHandlerSend404() throws ServletException, IOException {
        // given
        given(mockHandlerMappingRegistry.getHandler(any()))
            .willReturn(Optional.empty());
        RequestDispatcher mockDispatcher = mock(RequestDispatcher.class);
        given(request.getRequestDispatcher(any()))
            .willReturn(mockDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(request).getRequestDispatcher(urlCaptor.capture());
        verify(mockDispatcher, times(1)).forward(any(), any());
        assertThat(urlCaptor.getValue()).isEqualTo("/404.jsp");
    }

    @Test
    void noAdapterThrowException() throws ServletException, IOException {
        // given
        given(mockHandlerMappingRegistry.getHandler(any()))
            .willReturn(Optional.of(new Object()));
        given(mockHandlerAdapterRegistry.getHandlerAdapter(any()))
            .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
            .isInstanceOf(ServletException.class)
            .hasMessage("핸드러를 처리할 어댑터가 없습니다.");
    }

    @Test
    void renderViewFromAdapter() throws Exception {
        // given
        given(mockHandlerMappingRegistry.getHandler(any()))
            .willReturn(Optional.of(new Object()));

        HandlerAdapter mockHandlerAdapter = mock(HandlerAdapter.class);
        View mockView = mock(View.class);

        given(mockHandlerAdapter.handle(any(), any(), any()))
            .willReturn(new ModelAndView(mockView));

        given(mockHandlerAdapterRegistry.getHandlerAdapter(any()))
            .willReturn(Optional.of(mockHandlerAdapter));

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(mockView, times(1)).render(any(), any(), any());
    }
}
