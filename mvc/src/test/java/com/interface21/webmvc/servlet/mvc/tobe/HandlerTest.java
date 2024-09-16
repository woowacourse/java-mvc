package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerTest {

    @DisplayName("핸들러 생성한다.")
    @Test
    void constructTest() throws Exception {
        // given
        Method targetMethod = getClass().getDeclaredMethod(
            "testMethod", HttpServletRequest.class, HttpServletResponse.class);

        // when
        Handler handler = new Handler(targetMethod, new HandlerTest());

        // then
        assertAll(
            () -> assertThat(handler.getUri()).isEqualTo("/hello-cloud"),
            () -> assertThat(handler.getRequestMethods()).containsExactlyInAnyOrder(
                RequestMethod.GET, RequestMethod.DELETE)
        );
    }

    @DisplayName("메서드를 실행한다.")
    @Test
    void invokeTest() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/hello-cloud");
        when(request.getMethod()).thenReturn("GET");

        Method targetMethod = getClass().getDeclaredMethod(
            "testMethod", HttpServletRequest.class, HttpServletResponse.class);
        HandlerTest instance = spy(new HandlerTest());
        Handler handler = new Handler(targetMethod, instance);

        // when
        handler.handle(request, response);

        // then
        verify(instance).testMethod(any(), any());
    }

    @RequestMapping(value = "/hello-cloud", method = {RequestMethod.GET, RequestMethod.DELETE})
    void testMethod(HttpServletRequest request, HttpServletResponse response) {
        // nothing
    }
}
