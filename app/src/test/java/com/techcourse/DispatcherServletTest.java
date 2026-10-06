package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.ControllerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("DispatcherServlet")
class DispatcherServletTest {

    @Nested
    @DisplayName("초기화")
    class Initialization {

        @Test
        @DisplayName("등록된 모든 HandlerMapping을 초기화한다")
        void initializesEveryHandlerMapping() {
            // given
            final var first = mock(HandlerMapping.class);
            final var second = mock(HandlerMapping.class);
            final var servlet = new DispatcherServlet(List.of(first, second), List.of());

            // when
            servlet.init();

            // then
            verify(first).initialize();
            verify(second).initialize();
        }
    }

    @Nested
    @DisplayName("핸들러 선택")
    class HandlerSelection {

        private HttpServletRequest request;
        private HttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);
        }

        @Test
        @DisplayName("첫 매핑에서 찾으면 뒤의 매핑은 조회하지 않는다")
        void usesFirstMatchingHandlerMapping() throws Exception {
            // given
            final var handler = new Object();
            final var first = mock(HandlerMapping.class);
            final var second = mock(HandlerMapping.class);
            final var adapter = mock(HandlerAdapter.class);
            final var view = mock(View.class);
            when(first.getHandler(request)).thenReturn(handler);
            when(adapter.supports(handler)).thenReturn(true);
            when(adapter.handle(request, response, handler)).thenReturn(new ModelAndView(view));
            final var servlet = new DispatcherServlet(List.of(first, second), List.of(adapter));

            // when
            servlet.service(request, response);

            // then
            verifyNoInteractions(second);
        }

        @Test
        @DisplayName("첫 매핑이 찾지 못하면 다음 매핑을 조회한다")
        void usesNextMappingWhenPreviousOneMisses() throws Exception {
            // given
            final var handler = new Object();
            final var first = mock(HandlerMapping.class);
            final var second = mock(HandlerMapping.class);
            final var adapter = mock(HandlerAdapter.class);
            final var view = mock(View.class);
            when(second.getHandler(request)).thenReturn(handler);
            when(adapter.supports(handler)).thenReturn(true);
            when(adapter.handle(request, response, handler)).thenReturn(new ModelAndView(view));
            final var servlet = new DispatcherServlet(List.of(first, second), List.of(adapter));

            // when
            servlet.service(request, response);

            // then
            verify(adapter).handle(request, response, handler);
        }

        @Test
        @DisplayName("등록된 핸들러가 없으면 404를 응답한다")
        void sendsNotFoundWhenNoMappingMatches() throws Exception {
            // given
            final var mapping = mock(HandlerMapping.class);
            final var adapter = mock(HandlerAdapter.class);
            final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));

            // when
            servlet.service(request, response);

            // then
            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("핸들러 실행과 뷰 렌더링")
    class HandlerExecution {

        private HttpServletRequest request;
        private HttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);
        }

        @Test
        @DisplayName("지원하지 않는 어댑터를 건너뛰고 다음 어댑터를 실행한다")
        void skipsUnsupportedAdapter() throws Exception {
            // given
            final var handler = new Object();
            final var mapping = mock(HandlerMapping.class);
            final var unsupported = mock(HandlerAdapter.class);
            final var supported = mock(HandlerAdapter.class);
            final var view = mock(View.class);
            when(mapping.getHandler(request)).thenReturn(handler);
            when(supported.supports(handler)).thenReturn(true);
            when(supported.handle(request, response, handler)).thenReturn(new ModelAndView(view));
            final var servlet = new DispatcherServlet(List.of(mapping), List.of(unsupported, supported));

            // when
            servlet.service(request, response);

            // then
            verify(supported).handle(request, response, handler);
        }

        @Test
        @DisplayName("지원하는 첫 어댑터를 찾은 뒤에는 다른 어댑터를 조회하지 않는다")
        void stopsSearchingAfterFirstSupportingAdapter() throws Exception {
            // given
            final var handler = new Object();
            final var mapping = mock(HandlerMapping.class);
            final var first = mock(HandlerAdapter.class);
            final var later = mock(HandlerAdapter.class);
            final var view = mock(View.class);
            when(mapping.getHandler(request)).thenReturn(handler);
            when(first.supports(handler)).thenReturn(true);
            when(first.handle(request, response, handler)).thenReturn(new ModelAndView(view));
            final var servlet = new DispatcherServlet(List.of(mapping), List.of(first, later));

            // when
            servlet.service(request, response);

            // then
            verifyNoInteractions(later);
        }

        @Test
        @DisplayName("핸들러가 반환한 모델을 뷰에 한 번 전달한다")
        void rendersModelAndViewOnce() throws Exception {
            // given
            final var handler = new Object();
            final var mapping = mock(HandlerMapping.class);
            final var adapter = mock(HandlerAdapter.class);
            final var view = mock(View.class);
            when(mapping.getHandler(request)).thenReturn(handler);
            when(adapter.supports(handler)).thenReturn(true);
            when(adapter.handle(request, response, handler))
                    .thenReturn(new ModelAndView(view).addObject("id", "gugu"));
            final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));

            // when
            servlet.service(request, response);

            // then
            verify(view).render(Map.of("id", "gugu"), request, response);
        }

        @Test
        @DisplayName("핸들러를 지원하는 어댑터가 없으면 원인을 보존해 예외를 던진다")
        void preservesCauseWhenNoAdapterSupportsHandler() {
            // given
            final var handler = new Object();
            final var mapping = mock(HandlerMapping.class);
            final var adapter = mock(HandlerAdapter.class);
            when(mapping.getHandler(request)).thenReturn(handler);
            final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));

            // when & then
            assertThatThrownBy(() -> servlet.service(request, response))
                    .isInstanceOf(ServletException.class)
                    .hasCauseInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("핸들러 실행 예외의 원인을 보존한다")
        void preservesHandlerExecutionFailureAsCause() throws Exception {
            // given
            final var handler = new Object();
            final var mapping = mock(HandlerMapping.class);
            final var adapter = mock(HandlerAdapter.class);
            final var failure = new IllegalArgumentException("controller failed");
            when(mapping.getHandler(request)).thenReturn(handler);
            when(adapter.supports(handler)).thenReturn(true);
            when(adapter.handle(request, response, handler)).thenThrow(failure);
            final var servlet = new DispatcherServlet(List.of(mapping), List.of(adapter));

            // when & then
            assertThatThrownBy(() -> servlet.service(request, response))
                    .isInstanceOf(ServletException.class)
                    .hasCause(failure);
        }
    }

    @Nested
    @DisplayName("기존 방식과 어노테이션 방식의 공존")
    class Coexistence {

        private HttpServletRequest request;
        private HttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);
        }

        @Test
        @DisplayName("기본 생성자도 기존 Controller 요청을 처리한다")
        void defaultConstructorHandlesLegacyController() throws Exception {
            // given
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
            final var servlet = new DispatcherServlet();
            servlet.init();

            // when
            servlet.service(request, response);

            // then
            verify(dispatcher).forward(request, response);
        }

        @Test
        @DisplayName("어노테이션 컨트롤러 요청을 찾아 뷰로 이동한다")
        void handlesAnnotatedController() throws Exception {
            // given
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestURI()).thenReturn("/step2-annotation");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher("/step2-annotation.jsp")).thenReturn(dispatcher);
            final var servlet = new DispatcherServlet(
                    List.of(new ManualHandlerMapping(), new AnnotationHandlerMapping("com.techcourse.fixture.mvc")),
                    List.of(new ControllerAdapter(), new HandlerExecutionAdapter()));
            servlet.init();

            // when
            servlet.service(request, response);

            // then
            verify(dispatcher).forward(request, response);
        }
    }
}
