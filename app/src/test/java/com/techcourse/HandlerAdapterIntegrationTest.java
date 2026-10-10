package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.LegacyControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HandlerAdapterIntegrationTest {

    private HandlerMappingRegistry mappingRegistry;
    private HandlerAdapterRegistry adapterRegistry;

    @BeforeEach
    void setUp() {
        mappingRegistry = new HandlerMappingRegistry();
        mappingRegistry.addHandlerMapping(new ManualHandlerMapping());
        mappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        adapterRegistry = new HandlerAdapterRegistry();
        adapterRegistry.addHandlerAdapter(new LegacyControllerHandlerAdapter());
        adapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Test
    void annotationControllerRendersRegisterPageOnGet() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/sample/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        final Object handler = mappingRegistry.getHandler(request).orElseThrow();
        assertThat(handler).isInstanceOf(HandlerExecution.class);
        final HandlerAdapter adapter = adapterRegistry.getHandlerAdapter(handler);
        assertThat(adapter).isInstanceOf(AnnotationHandlerAdapter.class);

        final ModelAndView modelAndView = adapter.handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotationControllerRegistersUserOnPost() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final String account = "test-" + UUID.randomUUID();
        when(request.getRequestURI()).thenReturn("/sample/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("test@example.com");

        final Object handler = mappingRegistry.getHandler(request).orElseThrow();
        assertThat(handler).isInstanceOf(HandlerExecution.class);
        final HandlerAdapter adapter = adapterRegistry.getHandlerAdapter(handler);
        assertThat(adapter).isInstanceOf(AnnotationHandlerAdapter.class);

        final ModelAndView modelAndView = adapter.handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).sendRedirect("/index.jsp");
        assertThat(InMemoryUserRepository.findByAccount(account).map(User::getAccount)).contains(account);
    }

    @Test
    void legacyControllerStillRendersIndex() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        final Object handler = mappingRegistry.getHandler(request).orElseThrow();
        assertThat(handler).isInstanceOf(Controller.class);
        final HandlerAdapter adapter = adapterRegistry.getHandlerAdapter(handler);
        assertThat(adapter).isInstanceOf(LegacyControllerHandlerAdapter.class);

        final ModelAndView modelAndView = adapter.handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(dispatcher).forward(request, response);
    }
}
