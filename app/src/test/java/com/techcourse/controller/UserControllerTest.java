package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserControllerTest {

    private AnnotationHandlerMapping handlerMapping;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/api/user");
    }

    @Test
    void get() throws Exception {
        final var writer = new StringWriter();
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn("gugu");
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        final var handler = handlerMapping.getHandler(request).orElseThrow();
        final var handlerAdapter = new AnnotationHandlerAdapter();
        final var modelAndView = handlerAdapter.handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        assertThat(modelAndView.getObject("user"))
                .isSameAs(InMemoryUserRepository.findByAccount("gugu").orElseThrow());
        assertThat(writer.toString()).isEqualTo("{\"account\":\"gugu\"}");
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    @Test
    void missingAccount() throws Exception {
        assertError(null, HttpServletResponse.SC_BAD_REQUEST, "{\"message\":\"account는 필수입니다.\"}");
    }

    @Test
    void emptyAccount() throws Exception {
        assertError("", HttpServletResponse.SC_BAD_REQUEST, "{\"message\":\"account는 필수입니다.\"}");
    }

    @Test
    void blankAccount() throws Exception {
        assertError("   ", HttpServletResponse.SC_BAD_REQUEST, "{\"message\":\"account는 필수입니다.\"}");
    }

    @Test
    void unknownAccount() throws Exception {
        assertError("unknown-user", HttpServletResponse.SC_NOT_FOUND, "{\"message\":\"사용자를 찾을 수 없습니다.\"}");
    }

    @Test
    void postIsNotMapped() {
        when(request.getMethod()).thenReturn("POST");

        assertThat(handlerMapping.getHandler(request)).isEmpty();
    }

    private void assertError(final String account, final int status, final String expectedJson) throws Exception {
        final var writer = new StringWriter();
        when(request.getMethod()).thenReturn("GET");
        when(request.getParameter("account")).thenReturn(account);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        final var handler = handlerMapping.getHandler(request).orElseThrow();
        final var modelAndView = new AnnotationHandlerAdapter().handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        verify(response).setStatus(status);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(writer.toString()).isEqualTo(expectedJson);
    }
}
