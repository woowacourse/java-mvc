package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterTest {

    @Test
    @DisplayName("어노테이션 핸들러를 실행해 ModelAndView를 반환한다")
    void adaptsAnnotationHandler() throws Exception {
        // given
        final var method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final var handler = new HandlerExecution(new TestController(), method);
        final var adapter = new HandlerExecutionAdapter();
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        final var modelAndView = adapter.handle(handler, request, response);

        // then
        assertThat(adapter.supports(handler)).isTrue();
        assertThat(adapter.supports(new Object())).isFalse();
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
