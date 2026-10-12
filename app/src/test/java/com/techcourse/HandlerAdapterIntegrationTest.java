package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.LegacyControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        mappingRegistry.addHandlerMapping(new AnnotationHandlerMapping("com.techcourse.controller"));

        adapterRegistry = new HandlerAdapterRegistry();
        adapterRegistry.addHandlerAdapter(new LegacyControllerHandlerAdapter());
        adapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());
    }

    @Test
    void annotationControllerRendersRegisterPageOnGet() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/register/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        handleRequest(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotationControllerRegistersUserOnPost() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final String account = "test-" + UUID.randomUUID();
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn("password");
        when(request.getParameter("email")).thenReturn("test@example.com");

        handleRequest(request, response);

        verify(response).sendRedirect("/index.jsp");
        assertThat(InMemoryUserRepository.findByAccount(account).map(User::getAccount)).contains(account);
    }

    @Test
    void annotationControllerRendersIndex() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        handleRequest(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotationControllerRendersLoginPageOnGet() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final var dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        handleRequest(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void annotationLoginStoresUserInSession() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        final User user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");

        handleRequest(request, response);

        verify(session).setAttribute(UserSession.SESSION_KEY, user);
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void annotationLoginRedirectsToUnauthorizedForWrongPassword() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong-password");

        handleRequest(request, response);

        verify(session, never()).setAttribute(anyString(), any());
        verify(response).sendRedirect("/401.jsp");
    }

    @Test
    void annotationLogoutRemovesUserFromSession() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var session = mock(HttpSession.class);
        when(request.getRequestURI()).thenReturn("/logout");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);

        handleRequest(request, response);

        verify(session).removeAttribute(UserSession.SESSION_KEY);
        verify(response).sendRedirect("/");
    }

    @Test
    void loginAndRegistrationDoNotHandleGetRequests() {
        for (final String path : new String[] {"/login", "/register"}) {
            final var request = mock(HttpServletRequest.class);
            when(request.getRequestURI()).thenReturn(path);
            when(request.getMethod()).thenReturn("GET");

            assertThat(mappingRegistry.getHandler(request)).as("GET %s", path).isEmpty();
        }
    }

    private void handleRequest(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final Object handler = mappingRegistry.getHandler(request).orElseThrow();
        assertThat(handler).isInstanceOf(HandlerExecution.class);
        final HandlerAdapter adapter = adapterRegistry.getHandlerAdapter(handler);
        assertThat(adapter).isInstanceOf(AnnotationHandlerAdapter.class);

        final ModelAndView modelAndView = adapter.handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
