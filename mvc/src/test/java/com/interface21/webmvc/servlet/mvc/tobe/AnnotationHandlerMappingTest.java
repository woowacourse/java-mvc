package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.ModelAndView;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
    }

    @Nested
    @DisplayName("핸들러 조회 성공")
    class HasHandler {

        @Test
        @DisplayName("핸들러 존재 여부 조회: 참")
        void hasHandler() {
            final HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");

            assertThat(handlerMapping.hasHandler(request)).isTrue();
        }

        @Test
        @DisplayName("핸들러 존재 여부 조회: 거짓")
        void hasHandler_WhenNoHandler() {
            final HttpServletRequest request = mock(HttpServletRequest.class);

            when(request.getRequestURI()).thenReturn("/no-handler");
            when(request.getMethod()).thenReturn("GET");

            assertThat(handlerMapping.hasHandler(request)).isFalse();
        }
    }

    @Nested
    @DisplayName("핸들러 조회 성공")
    class GetHandler {

        @Test
        @DisplayName("핸들러 조회 성공: GET /get-test 요청")
        void get() {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");

            final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            final ModelAndView modelAndView = handlerExecution.handle(request, response);

            assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        }

        @Test
        @DisplayName("핸들러 조회 성공: POST /post-test 요청")
        void post() {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/post-test");
            when(request.getMethod()).thenReturn("POST");

            final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
            final ModelAndView modelAndView = handlerExecution.handle(request, response);

            assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        }

        @Test
        @DisplayName("핸들러 조회 실패: 핸들러가 등록되지 않는 URL 요청")
        void onlyUrl_noMethod() {
            final var request = mock(HttpServletRequest.class);

            when(request.getAttribute("id")).thenReturn("gugu");
            when(request.getRequestURI()).thenReturn("/no-method");
            when(request.getMethod()).thenReturn("GET");

            assertThatThrownBy(() -> handlerMapping.getHandler(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Annotation Mapping에 해당 요청을 처리할 수 있는 핸들러가 없습니다.");
        }
    }
}
