package com.techcourse.controller;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.RequestMethodNotSupportedException;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterControllerTest {

    private AnnotationHandlerMapping annotationHandlerMapping;
    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerExecutor handlerExecutor;

    @BeforeEach
    void setUp() {
        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();

        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerExecutor = new HandlerExecutor(handlerAdapterRegistry);
    }

    @Test
    void getRegisterForwardsToRegistrationPage() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        ModelAndView modelAndView = execute(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void postRegisterSavesUserAndRedirectsToIndex() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String account = "register-test-" + UUID.randomUUID();
        String password = "password123";

        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn(account);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn("register-test@example.com");

        ModelAndView modelAndView = execute(request, response);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        assertThat(InMemoryUserRepository.findByAccount(account))
                .hasValueSatisfying(user -> assertThat(user.checkPassword(password)).isTrue());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void unsupportedMethodForRegisterIsRejected() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/register");
        when(request.getMethod()).thenReturn("PUT");

        assertThatThrownBy(() -> annotationHandlerMapping.getHandler(request))
                .isInstanceOf(RequestMethodNotSupportedException.class)
                .satisfies(exception -> {
                    RequestMethodNotSupportedException actual = (RequestMethodNotSupportedException) exception;
                    assertThat(actual.getSupportedMethods())
                            .containsExactlyInAnyOrder(RequestMethod.GET, RequestMethod.POST);
                });
    }

    private ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object handler = handlerMappingRegistry.getHandler(request)
                .orElseThrow(() -> new AssertionError("No handler found for /register"));
        return handlerExecutor.execute(handler, request, response);
    }
}
