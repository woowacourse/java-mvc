package com.techcourse.controller.annotation;

import static com.techcourse.controller.UserSession.SESSION_KEY;
import static nextstep.mvc.view.JspView.REDIRECT_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.lang.reflect.Field;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginControllerTest {

    private HandlerMapping handlerMapping = new AnnotationHandlerMapping("com.techcourse");

    @BeforeEach
    void setUp() {
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("세션이 없을때 로그인 요청 성공")
    void execute_NoSession_Success() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getSession()).thenReturn(session);

        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handler.handle(request, response);

        Class<? extends View> aClass = modelAndView.getView().getClass();
        Field field = aClass.getDeclaredField("viewName");
        field.setAccessible(true);

        assertThat(field.get(modelAndView.getView())).isEqualTo(REDIRECT_PREFIX + "/index.jsp");
    }

    @Test
    @DisplayName("세션이 없을때 로그인 요청 실패")
    void execute_NoSession_Fail() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getParameter("password")).thenReturn("wrong");
        when(request.getSession()).thenReturn(session);

        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handler.handle(request, response);

        Class<? extends View> aClass = modelAndView.getView().getClass();
        Field field = aClass.getDeclaredField("viewName");
        field.setAccessible(true);

        assertThat(field.get(modelAndView.getView())).isEqualTo(REDIRECT_PREFIX + "/401.jsp");
    }

    @Test
    @DisplayName("세션이 있을때 로그인 요청시")
    void execute_Session() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User user = mock(User.class);

        when(request.getRequestURI()).thenReturn("/login");
        when(request.getMethod()).thenReturn("POST");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_KEY)).thenReturn(user);

        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handler.handle(request, response);

        Class<? extends View> aClass = modelAndView.getView().getClass();
        Field field = aClass.getDeclaredField("viewName");
        field.setAccessible(true);

        assertThat(field.get(modelAndView.getView())).isEqualTo(REDIRECT_PREFIX + "/index.jsp");
    }
}
