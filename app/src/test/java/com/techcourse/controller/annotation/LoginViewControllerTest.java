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

class LoginViewControllerTest {

    private HandlerMapping handlerMapping = new AnnotationHandlerMapping("com.techcourse");

    @BeforeEach
    void setUp() {
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("로그인 되어있지 않을 때 로그인 페이지 접근")
    void execute() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(SESSION_KEY)).thenReturn(null);

        HandlerExecution handler = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handler.handle(request, response);

        Class<? extends View> aClass = modelAndView.getView().getClass();
        Field field = aClass.getDeclaredField("viewName");
        field.setAccessible(true);

        assertThat(field.get(modelAndView.getView())).isEqualTo("/login.jsp");
    }

    @Test
    @DisplayName("로그인 되어있을 때 로그인 페이지 접근")
    void execute_Login() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        User user = mock(User.class);

        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");
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
