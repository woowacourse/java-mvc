package com.techcourse.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PageControllerTest {

    private AnnotationHandlerMapping handlerMapping;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        requestDispatcher = mock(RequestDispatcher.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void home() throws Exception {
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        render("/");

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void loginView() throws Exception {
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);

        render("/login/view");

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void loggedInLoginView() throws Exception {
        final var user = InMemoryUserRepository.findByAccount("gugu").orElseThrow();
        when(session.getAttribute(UserSession.SESSION_KEY)).thenReturn(user);

        render("/login/view");

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void registerView() throws Exception {
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        render("/register/view");

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void register() throws Exception {
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        render("/register");

        verify(requestDispatcher).forward(request, response);
    }

    private void render(final String requestURI) throws Exception {
        when(request.getRequestURI()).thenReturn(requestURI);

        final var handler = handlerMapping.getHandler(request).orElseThrow();
        final var modelAndView = new AnnotationHandlerAdapter().handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
