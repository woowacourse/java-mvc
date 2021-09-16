package com.techcourse.controller;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.servlet.AnnotationHandlerMapping;
import nextstep.mvc.servlet.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        try {
            handlerMapping.initialize();
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void show() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getParameter("account")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("GET");

        User user = new User(1L, "gugu", "1234", "gugu@gugu");
        InMemoryUserRepository.save(user);

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);
        modelAndView.render(request, response);

        assertThat(modelAndView.getObject("user")).usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void showAll() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/users");
        when(request.getMethod()).thenReturn("GET");

        User user = new User(1L, "gugu", "1234", "gugu@gugu");
        User user2 = new User(2L, "sally", "1234", "sally@sally");
        InMemoryUserRepository.save(user);
        InMemoryUserRepository.save(user2);

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);
        modelAndView.render(request, response);

        Map<String, Object> model = modelAndView.getModel();
        assertThat(model).hasSize(2);
        assertThat(model.get("gugu")).usingRecursiveComparison()
                .isEqualTo(user);
        assertThat(model.get("sally")).usingRecursiveComparison()
                .isEqualTo(user2);
    }
}
