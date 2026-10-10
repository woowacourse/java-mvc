package com.techcourse.controller;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutor;
import com.interface21.webmvc.servlet.mvc.tobe.RequestMethodNotSupportedException;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private AnnotationHandlerMapping handlerMapping;
    private HandlerExecutor handlerExecutor;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        handlerMapping.initialize();

        final HandlerAdapterRegistry handlerAdapters = new HandlerAdapterRegistry();
        handlerAdapters.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        handlerExecutor = new HandlerExecutor(handlerAdapters);

        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");
    }

    @Test
    void GET_요청의_account로_조회한_사용자를_JSON으로_응답한다() throws Exception {
        final String account = "user-api-test-" + UUID.randomUUID();
        final User user = new User(3, account, "password", "user-api@example.com");
        InMemoryUserRepository.save(user);
        when(request.getParameter("account")).thenReturn(account);
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        final Object handler = handlerMapping.getHandler(request);
        assertThat(handler).isNotNull();
        final ModelAndView modelAndView = handlerExecutor.execute(handler, request, response);
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);

        assertThat(modelAndView.getObject("user")).isSameAs(user);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"account\":\"" + account + "\"}");
    }

    @Test
    void 존재하지_않는_계정은_404와_JSON_오류를_응답한다() throws Exception {
        when(request.getParameter("account")).thenReturn("missing-user-" + UUID.randomUUID());

        assertErrorResponse(HttpServletResponse.SC_NOT_FOUND, "user not found");
    }

    @Test
    void 계정_파라미터가_없으면_400과_JSON_오류를_응답한다() throws Exception {
        assertErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "account parameter is required");
    }

    @Test
    void 계정이_빈_문자열이면_400과_JSON_오류를_응답한다() throws Exception {
        when(request.getParameter("account")).thenReturn("");

        assertErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "account parameter is required");
    }

    @Test
    void 계정이_공백이면_400과_JSON_오류를_응답한다() throws Exception {
        when(request.getParameter("account")).thenReturn(" \t ");

        assertErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "account parameter is required");
    }

    @Test
    void POST_요청은_사용자_조회에_매핑하지_않는다() {
        when(request.getMethod()).thenReturn("POST");

        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(RequestMethodNotSupportedException.class);
    }

    private void assertErrorResponse(final int status, final String message) throws Exception {
        final StringWriter body = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(body));

        final ModelAndView modelAndView = new UserController().show(request, response);
        final View view = modelAndView.getView();
        final Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);

        verify(response).setStatus(status);
        verify(response).setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        assertThat(body.toString()).isEqualTo("{\"message\":\"" + message + "\"}");
    }
}
