package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RequestMappingHandlerAdapterTest {

    private RequestMappingHandlerAdapter adapter;
    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws Exception {
        adapter = new RequestMappingHandlerAdapter();
        final Method method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        handlerExecution = new HandlerExecution(new TestController(), method);
    }

    @Test
    void HandlerExecution은_지원한다() {
        assertThat(adapter.supports(handlerExecution)).isTrue();
    }

    @Test
    void HandlerExecution이_아니면_지원하지_않는다() {
        final Object handler = new Object();

        assertThat(adapter.supports(handler)).isFalse();
    }

    @Test
    void HandlerExecution을_실행하고_그_결과인_ModelAndView를_반환한다() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        final ModelAndView modelAndView = (ModelAndView) adapter.handle(request, response, handlerExecution);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
