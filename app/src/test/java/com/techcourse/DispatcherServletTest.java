package com.techcourse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapterRegistry;
import webmvc.org.springframework.web.servlet.mvc.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.HandlerMappingRegistry;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JsonView;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class DispatcherServletTest {

    private HandlerMappingRegistry handlerMappings;

    private HandlerAdapterRegistry handlerAdpaters;

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        handlerMappings = mock(HandlerMappingRegistry.class);
        handlerAdpaters = mock(HandlerAdapterRegistry.class);
        dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdpaters);
    }

    @DisplayName("DispatcherServlet 초기화 시 HandlerMapping과 HandlerAdapter를 등록한다.")
    @Test
    void init() {
        // when
        dispatcherServlet.init();

        // then
        verify(handlerMappings, times(2)).addHandlerMapping(any(HandlerMapping.class));
        verify(handlerAdpaters, times(2)).addHandlerAdapter(any(HandlerAdapter.class));
    }

    @DisplayName("DispatcherServlet은 요청에 맞는 Handler를 찾아서 처리한다.")
    @Test
    void service() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        Controller controller = mock(Controller.class);
        HandlerAdapter handlerAdapter = mock(HandlerAdapter.class);

        given(handlerMappings.getHandler(request)).willReturn(Optional.of(controller));
        given(handlerAdpaters.getHandlerAdapter(controller)).willReturn(handlerAdapter);
        given(handlerAdapter.handle(request, response, controller)).willReturn(new ModelAndView(new JsonView()));

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(handlerMappings, times(1)).getHandler(request);
        verify(handlerAdpaters, times(1)).getHandlerAdapter(any());
    }

    @DisplayName("요청한 리소스가 없을 경우 404를 반환한다.")
    @Test
    void service_NotFound() throws ServletException, IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        given(handlerMappings.getHandler(request)).willReturn(Optional.empty());

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(handlerMappings, times(1)).getHandler(request);
        verify(response, times(1)).setStatus(404);
        verify(response, times(1)).sendRedirect("404.jsp");
    }
}
