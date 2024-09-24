package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.asis.Controller;

class ManualHandlerMappingTest {

    @Nested
    @DisplayName("요청을 처리할 수 있는 핸들러 여부 조회")
    class CanHandle {

        @Test
        @DisplayName("요청을 처리할 수 있는 핸들러 여부 조회: 참")
        void canHandle() {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");

            assertTrue(manualHandlerMapping.hasHandler(request));
        }

        @Test
        @DisplayName("요청을 처리할 수 있는 핸들러 여부 조회: ManualHandlerMapping이 처리할 수 없는 경우 거짓")
        void canHandle_WhenRequestNotFitManualHandlerMapping() {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

            when(request.getRequestURI()).thenReturn("/register");
            when(request.getMethod()).thenReturn("GET");

            assertFalse(manualHandlerMapping.hasHandler(request));
        }

        @Test
        @DisplayName("요청을 처리할 수 있는 핸들러 여부 조회: 등록되지 않은 핸들러 요청인 경우 거짓")
        void canNotHandle() {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

            when(request.getRequestURI()).thenReturn("/allhaha");
            when(request.getMethod()).thenReturn("GET");

            assertFalse(manualHandlerMapping.hasHandler(request));
        }
    }

    @Nested
    @DisplayName("요청에 맞는 핸들러 반환 성공")
    class GetHandler {

        @Test
        @DisplayName("요청에 맞는 핸들러 반환 성공")
        void getHandler() throws Exception {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");

            final Controller controller = manualHandlerMapping.getHandler(request);
            assertThat(controller.execute(request, null)).isEqualTo("/index.jsp");

        }

        @Test
        @DisplayName("요청에 맞는 핸들러 반환 실패: 수동 등록되지 않은 핸들러 요청인 경우")
        void getHandler_When() {
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();

            when(request.getRequestURI()).thenReturn("/asdf");
            when(request.getMethod()).thenReturn("GET");

            assertThatThrownBy(() -> manualHandlerMapping.getHandler(request))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("ManualHandlerMapping 에 요청을 처리할 수 있는 핸들러가 없습니다. 핸들러를 등록해주세요.");
        }
    }
}
