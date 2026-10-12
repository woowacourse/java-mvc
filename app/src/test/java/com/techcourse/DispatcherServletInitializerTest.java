package com.techcourse;

import com.interface21.webmvc.servlet.DispatcherServlet;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("앱의 DispatcherServlet 구성과 등록")
class DispatcherServletInitializerTest {

    @Nested
    @DisplayName("서블릿 등록")
    class ServletRegistrationConfiguration {

        private DispatcherServletInitializer initializer;
        private ServletContext context;
        private ServletRegistration.Dynamic registration;

        @BeforeEach
        void setUp() {
            initializer = new DispatcherServletInitializer();
            context = mock(ServletContext.class);
            registration = mock(ServletRegistration.Dynamic.class);
            when(context.addServlet(eq("dispatcher"), any(Servlet.class))).thenReturn(registration);
        }

        @Test
        @DisplayName("MVC 모듈의 DispatcherServlet을 컨테이너에 등록한다")
        void registersFrameworkDispatcherServlet() {
            // given
            final var servlet = ArgumentCaptor.forClass(Servlet.class);

            // when
            initializer.onStartup(context);

            // then
            verify(context).addServlet(eq("dispatcher"), servlet.capture());
            assertThat(servlet.getValue()).isInstanceOf(DispatcherServlet.class);
        }

        @Test
        @DisplayName("서버 시작 시 서블릿을 초기화하도록 설정한다")
        void configuresLoadOnStartup() {
            // when
            initializer.onStartup(context);

            // then
            verify(registration).setLoadOnStartup(1);
        }

        @Test
        @DisplayName("루트 경로에 서블릿을 매핑한다")
        void mapsServletToRoot() {
            // when
            initializer.onStartup(context);

            // then
            verify(registration).addMapping("/");
        }

        @Test
        @DisplayName("같은 이름으로 서블릿을 등록할 수 없으면 실패한다")
        void rejectsDuplicateServletName() {
            // given
            when(context.addServlet(eq("dispatcher"), any(Servlet.class))).thenReturn(null);

            // when & then
            assertThatThrownBy(() -> initializer.onStartup(context))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("dispatcher");
        }
    }

    @Nested
    @DisplayName("앱의 컨트롤러 구성")
    class ApplicationControllerConfiguration {

        private Servlet servlet;
        private HttpServletRequest request;
        private HttpServletResponse response;

        @BeforeEach
        void setUp() throws ServletException {
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);
            final var context = mock(ServletContext.class);
            when(context.addServlet(eq("dispatcher"), any(Servlet.class)))
                    .thenReturn(mock(ServletRegistration.Dynamic.class));
            new DispatcherServletInitializer().onStartup(context);
            final var capturedServlet = ArgumentCaptor.forClass(Servlet.class);
            verify(context).addServlet(eq("dispatcher"), capturedServlet.capture());
            servlet = capturedServlet.getValue();
            servlet.init(mock(ServletConfig.class));
        }

        @Test
        @DisplayName("등록한 서블릿은 GET / 요청을 홈 컨트롤러로 연결한다")
        void handlesAnnotatedHomeController() throws Exception {
            // given
            final var dispatcher = mock(RequestDispatcher.class);
            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

            // when
            servlet.service(request, response);

            // then
            verify(dispatcher).forward(request, response);
        }

        @DisplayName("GET 이외의 홈 요청은 매핑이 없어 404를 응답한다")
        @ParameterizedTest(name = "[{index}] {0} / → 매핑 없음")
        @ValueSource(strings = {"POST", "PUT", "DELETE"})
        void rejectsUnsupportedHomeMethod(final String method) throws Exception {
            // given
            when(request.getRequestURI()).thenReturn("/");
            when(request.getMethod()).thenReturn(method);

            // when
            servlet.service(request, response);

            // then
            verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        @Test
        @DisplayName("등록한 서블릿은 POST /login 요청을 로그인 컨트롤러로 연결한다")
        void handlesAnnotatedLoginController() throws Exception {
            // given
            final var session = mock(HttpSession.class);
            final var user = new User(42, "learner", "password", "learner@example.test");
            when(request.getRequestURI()).thenReturn("/login");
            when(request.getMethod()).thenReturn("POST");
            when(request.getSession()).thenReturn(session);
            when(request.getParameter("account")).thenReturn("learner");
            when(request.getParameter("password")).thenReturn("password");
            try (final var repository = mockStatic(InMemoryUserRepository.class)) {
                repository.when(() -> InMemoryUserRepository.findByAccount("learner"))
                        .thenReturn(Optional.of(user));

                // when
                servlet.service(request, response);

                // then
                verify(response).sendRedirect("/index.jsp");
            }
        }
    }
}
