package com.interface21.webmvc.servlet.view;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("JSP 뷰 렌더링")
class JspViewTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Nested
    @DisplayName("JSP로 전달할 때")
    class Forward {

        @DisplayName("지정한 JSP 경로로 같은 요청과 응답을 전달한다")
        @ParameterizedTest(name = "[{index}] JSP 경로={0}")
        @ValueSource(strings = {"/index.jsp", "/login.jsp"})
        void forwardsToJsp(final String viewName) throws Exception {
            // given
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher(viewName)).thenReturn(dispatcher);
            final var view = new JspView(viewName);

            // when
            view.render(Map.of(), request, response);

            // then
            verify(dispatcher).forward(request, response);
        }

        @DisplayName("JSP로 전달하기 전에 모델 값을 요청 속성에 넣는다")
        @ParameterizedTest(name = "[{index}] 모델 속성 {0}={1}")
        @CsvSource({"id, gugu", "title, 게시글 제목"})
        void exposesModelBeforeForward(final String key, final String value) throws Exception {
            // given
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestDispatcher("/user.jsp")).thenReturn(dispatcher);
            final var view = new JspView("/user.jsp");
            final var model = Map.of(key, value);

            // when
            view.render(model, request, response);

            // then
            final var order = inOrder(request, dispatcher);
            order.verify(request).setAttribute(key, value);
            order.verify(dispatcher).forward(request, response);
        }
    }

    @Nested
    @DisplayName("리다이렉트할 때")
    class Redirect {

        @DisplayName("redirect: 접두사를 제거한 주소로 리다이렉트한다")
        @ParameterizedTest(name = "[{index}] 리다이렉트 목적지={0}")
        @ValueSource(strings = {"/index.jsp", "/login"})
        void redirectsToLocation(final String location) throws Exception {
            // given
            final var view = new JspView("redirect:" + location);

            // when
            view.render(Map.of(), request, response);

            // then
            verify(response).sendRedirect(location);
        }

        @Test
        @DisplayName("리다이렉트 이후 JSP 전달을 진행하지 않는다")
        void doesNotForwardAfterRedirect() throws Exception {
            // given
            final var view = new JspView("redirect:/index.jsp");

            // when
            view.render(Map.of(), request, response);

            // then
            verify(request, never()).getRequestDispatcher(anyString());
        }
    }
}
