package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionHandlerAdapterTest {

    private HandlerExecutionHandlerAdapter handlerAdapter;
    private HandlerExecution handlerExecution;

    @BeforeEach
    void setUp() throws Exception {
        handlerAdapter = new HandlerExecutionHandlerAdapter();
        handlerExecution = new HandlerExecution(
                new TestController(),
                TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class)
        );
    }

    @Test
    @DisplayName("HandlerExecution을 처리할 수 있다")
    void handleable_withHandlerExecution() {
        assertTrue(handlerAdapter.handleable(handlerExecution));
    }

    @Test
    @DisplayName("HandlerExecution이 아니면 처리할 수 없다")
    void handleable_withNonHandlerExecution() {
        assertFalse(handlerAdapter.handleable(new ForwardController("/index.jsp")));
    }

    @Test
    @DisplayName("HandlerExecution을 실행한 ModelAndView를 그대로 반환한다")
    void execute() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        ModelAndView modelAndView = handlerAdapter.execute(request, response, handlerExecution);

        // then
        assertEquals("gugu", modelAndView.getObject("id"));
    }
}
