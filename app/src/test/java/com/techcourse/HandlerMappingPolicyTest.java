package com.techcourse;

import com.interface21.webmvc.servlet.mvc.asis.ControllerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import com.techcourse.controller.UserSession;
import com.techcourse.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("어노테이션과 수동 매핑이 같은 경로를 처리하는 정책")
class HandlerMappingPolicyTest {

    private DispatcherServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        servlet = new DispatcherServlet(
                List.of(new AnnotationHandlerMapping("com.techcourse.fixture.mappingpolicy"),
                        new ManualHandlerMapping()),
                List.of(new HandlerExecutionAdapter(), new ControllerAdapter()));
        servlet.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/login");
    }

    @Nested
    @DisplayName("어노테이션 매핑이 메서드까지 일치하면")
    class MatchingAnnotatedMethod {

        @Test
        @DisplayName("GET /login은 먼저 등록한 어노테이션 컨트롤러가 처리한다")
        void annotationMappingWinsForGetLogin() throws Exception {
            // given
            when(request.getMethod()).thenReturn("GET");

            // when
            servlet.service(request, response);

            // then
            verify(response).sendRedirect("/annotated-login");
        }
    }

    @Nested
    @DisplayName("어노테이션 매핑이 메서드에 일치하지 않으면")
    class UnmatchedAnnotatedMethod {

        @ParameterizedTest(name = "[{index}] {0} /login → 기존 Controller 폴백")
        @ValueSource(strings = {"POST", "PUT"})
        @DisplayName("같은 경로라도 기존 수동 Controller가 처리한다")
        void fallsBackToLegacyControllerForOtherMethods(final String method) throws Exception {
            // given
            when(request.getMethod()).thenReturn(method);
            final var session = mock(HttpSession.class);
            when(request.getSession()).thenReturn(session);
            when(session.getAttribute(UserSession.SESSION_KEY))
                    .thenReturn(new User(99, "already-logged-in", "unused-password", "unused@example.test"));

            // when
            servlet.service(request, response);

            // then
            verify(response).sendRedirect("/index.jsp");
        }
    }
}
