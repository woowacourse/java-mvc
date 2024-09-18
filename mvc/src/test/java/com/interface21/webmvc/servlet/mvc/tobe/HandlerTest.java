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
        Method targetMethod = getTargetMethod("testMethod");

        // when
        Handler handler = new Handler(targetMethod, new HandlerTest());

        // then
        assertAll(
            () -> assertThat(handler.getUri()).isEqualTo("/hello-cloud"),
            () -> assertThat(handler.getRequestMethods()).containsExactlyInAnyOrder(
                RequestMethod.GET, RequestMethod.DELETE)
        );
    }

    @DisplayName("맵핑되는 요청 메서드가 없으면, 모든 요청을 처리할 수 있다.")
    @Test
    void constructTest1() throws Exception {
        // given
        Method targetMethod = getTargetMethod("noRequestMethod");

        // when
        Handler handler = new Handler(targetMethod, new HandlerTest());

        // then
        assertAll(
            () -> assertThat(handler.getUri()).isEqualTo("/hello-mangcho"),
            () -> assertThat(handler.getRequestMethods()).containsExactlyInAnyOrder(
                RequestMethod.values())
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

        Method targetMethod = getTargetMethod("testMethod");
        HandlerTest instance = spy(new HandlerTest());
        Handler handler = new Handler(targetMethod, instance);

        // when
        handler.handle(request, response);

        // then
        verify(instance).testMethod(any(), any());
    }

    @DisplayName("메서드 직접 실행과 method.invoke()의 성능 차이를 비교한다.")
    @Test
    void speedTestBetweenDirectCallAndMethodInvoke() throws Exception {
        int METHOD_CALL_COUNT = 1_000_000;

        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/hello-cloud");
        when(request.getMethod()).thenReturn("GET");

        Method targetMethod = getTargetMethod("testMethod");
        HandlerTest instance = spy(new HandlerTest());
        Handler handler = new Handler(targetMethod, instance);

        // when
        long methodReflectionInvokeTime = speedMeasure(() -> {
            for (int i = 0; i < METHOD_CALL_COUNT; i++) {
                try {
                    handler.handle(request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        long methodDirectCallTime = speedMeasure(() -> {
            HandlerTest handlerTest = new HandlerTest();
            for (int i = 0; i < METHOD_CALL_COUNT; i++) {
                handlerTest.testMethod(request, response);
            }
        });

        // then
        assertThat(methodDirectCallTime).isLessThan(methodReflectionInvokeTime);
    }

    private Method getTargetMethod(String methodName) throws NoSuchMethodException {
        return getClass().getDeclaredMethod(
            methodName, HttpServletRequest.class, HttpServletResponse.class);
    }

    private long speedMeasure(Runnable runnable) {
        long startTime = System.currentTimeMillis();
        runnable.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    @RequestMapping(value = "/hello-cloud", method = {RequestMethod.GET, RequestMethod.DELETE})
    void testMethod(HttpServletRequest request, HttpServletResponse response) {
        // nothing
    }

    @RequestMapping(value = "/hello-mangcho")
    void noRequestMethod(HttpServletRequest request, HttpServletResponse response) {
        // nothing
    }
}
