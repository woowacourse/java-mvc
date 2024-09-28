package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterContainer;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingContainer;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

class DispatcherServletTest {

    private static final String BASE_PACKAGE = "com";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.dispatcherServlet = new DispatcherServlet(
                new HandlerMappingContainer(BASE_PACKAGE),
                new HandlerAdapterContainer(BASE_PACKAGE)
        );
        dispatcherServlet.init();

    }

    @Nested
    @DisplayName("service 로직 테스트")
    class Service {

        @Nested
        @DisplayName("Legacy MVC")
        class LegacyMVC {
            @Test
            @DisplayName("GET / -> /index.jsp")
            void service_success_get_root() throws IOException, ServletException {
                String path = "/index.jsp";
                when(request.getRequestURI()).thenReturn("/");
                when(request.getMethod()).thenReturn("GET");
                RequestDispatcher dispatcher = mock(RequestDispatcher.class);
                when(request.getRequestDispatcher(path)).thenReturn(dispatcher);

                dispatcherServlet.service(request, response);

                verify(request).getRequestDispatcher(path);
                verify(dispatcher).forward(request, response);
                verify(response, never()).sendRedirect(anyString());
            }

            @Test
            @DisplayName("GET /register/view -> /register.jsp")
            void service_success_get_register() throws ServletException, IOException {
                String path = "/register.jsp";
                when(request.getRequestURI()).thenReturn("/register/view");
                when(request.getMethod()).thenReturn("GET");
                RequestDispatcher dispatcher = mock(RequestDispatcher.class);
                when(request.getRequestDispatcher(path)).thenReturn(dispatcher);

                dispatcherServlet.service(request, response);

                verify(request).getRequestDispatcher(path);
                verify(dispatcher).forward(request, response);
                verify(response, never()).sendRedirect(anyString());
            }

            @Nested
            @DisplayName("POST /register")
            class ServiceRegister {

                @Test
                @DisplayName("POST /register 회원가입 성공 -> /index.jsp")
                void service_success_post_register() throws IOException {
                    String path = "/index.jsp";
                    when(request.getRequestURI()).thenReturn("/register");
                    when(request.getMethod()).thenReturn("POST");
                    when(request.getParameter("account")).thenReturn("new account");
                    when(request.getParameter("password")).thenReturn("new password");
                    when(request.getParameter("email")).thenReturn("new email");

                    dispatcherServlet.service(request, response);

                    verify(response).sendRedirect(path);
                    verify(request, never()).getRequestDispatcher(anyString());
                    assertThat(InMemoryUserRepository.findByAccount("new account"))
                            .isNotEmpty();
                }

                @Nested
                @DisplayName("POST /register 회원가입에 필요한 파라미터가 없을 경우 -> /400.jsp")
                class ServiceRegisterNotContainParams {

                    private static final String REDIRECT_400_JSP = "/400.jsp";

                    private void setUpRegisterRequest() {
                        when(request.getRequestURI()).thenReturn("/register");
                        when(request.getMethod()).thenReturn("POST");
                    }

                    @Test
                    @DisplayName("이메일이 없는 경우")
                    void not_contain_email_400() throws IOException {
                        when(request.getParameter("account")).thenReturn("new account");
                        when(request.getParameter("password")).thenReturn("new password");
                        setUpRegisterRequest();

                        dispatcherServlet.service(request, response);

                        verify(response).sendRedirect(REDIRECT_400_JSP);
                        verify(request, never()).getRequestDispatcher(anyString());
                    }

                    @Test
                    @DisplayName("아이디가 없는 경우")
                    void not_contain_account_400() throws IOException {
                        when(request.getParameter("password")).thenReturn("new password");
                        when(request.getParameter("email")).thenReturn("new email");
                        setUpRegisterRequest();

                        dispatcherServlet.service(request, response);

                        verify(response).sendRedirect(REDIRECT_400_JSP);
                        verify(request, never()).getRequestDispatcher(anyString());
                    }

                    @Test
                    @DisplayName("아이디가 없는 경우")
                    void not_contain_password_400() throws IOException {
                        when(request.getParameter("account")).thenReturn("new account");
                        when(request.getParameter("email")).thenReturn("new email");
                        setUpRegisterRequest();

                        dispatcherServlet.service(request, response);

                        verify(response).sendRedirect(REDIRECT_400_JSP);
                        verify(request, never()).getRequestDispatcher(anyString());
                    }
                }
            }

            @Test
            @DisplayName("GET /logout -> 세션 정보 삭제")
            void service_success_get_logout() throws IOException {
                String path = "/";
                when(request.getRequestURI()).thenReturn("/logout");
                when(request.getMethod()).thenReturn("GET");
                HttpSession session = mock(HttpSession.class);
                when(request.getSession()).thenReturn(session);

                dispatcherServlet.service(request, response);

                verify(response).sendRedirect(path);
                verify(request, never()).getRequestDispatcher(anyString());
                verify(session).removeAttribute(anyString());
            }

            @Nested
            @DisplayName("GET /login/view")
            class ServiceLoginView {

                @Test
                @DisplayName("GET /login/view 로그인 안 된 경우 -> /login.jsp")
                void service_success_get_login_not_login() throws IOException, ServletException {
                    String path = "/login.jsp";
                    when(request.getRequestURI()).thenReturn("/login/view");
                    when(request.getMethod()).thenReturn("GET");
                    HttpSession session = mock(HttpSession.class);
                    when(request.getSession()).thenReturn(session);
                    RequestDispatcher dispatcher = mock(RequestDispatcher.class);
                    when(request.getRequestDispatcher(path)).thenReturn(dispatcher);

                    dispatcherServlet.service(request, response);

                    verify(request).getRequestDispatcher(path);
                    verify(dispatcher).forward(request, response);
                    verify(response, never()).sendRedirect(anyString());
                }

                @Test
                @DisplayName("GET /login/view 로그인 된 경우 -> /index.jsp")
                void service_success_get_login() throws IOException {
                    // given
                    String path = "/index.jsp";
                    when(request.getRequestURI()).thenReturn("/login/view");
                    when(request.getMethod()).thenReturn("GET");

                    HttpSession session = mock(HttpSession.class);
                    var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
                    when(session.getAttribute("user")).thenReturn(user);
                    when(request.getSession()).thenReturn(session);

                    // then
                    dispatcherServlet.service(request, response);

                    // when
                    verify(response).sendRedirect(path);
                    verify(request, never()).getRequestDispatcher(anyString());
                }
            }
        }

        @Nested
        @DisplayName("@MVC")
        class AnnotationMVC {

            @Nested
            @DisplayName("GET /login")
            class ServiceGetLogin {

                @Test
                @DisplayName("로그인 안 된 경우 -> /login.jsp")
                void service_success_get_login_not_login() throws IOException {
                    String path = "/login.jsp";
                    when(request.getRequestURI()).thenReturn("/login");
                    when(request.getMethod()).thenReturn("GET");
                    HttpSession session = mock(HttpSession.class);
                    when(request.getSession()).thenReturn(session);

                    dispatcherServlet.service(request, response);

                    verify(response).sendRedirect(path);
                    verify(request, never()).getRequestDispatcher(anyString());
                }

                @Test
                @DisplayName("로그인 된 경우 -> /index.jsp")
                void service_success_get_login() throws IOException {
                    // given
                    String path = "/index.jsp";
                    when(request.getRequestURI()).thenReturn("/login");
                    when(request.getMethod()).thenReturn("GET");

                    HttpSession session = mock(HttpSession.class);
                    var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
                    when(session.getAttribute("user")).thenReturn(user);
                    when(request.getSession()).thenReturn(session);

                    // then
                    dispatcherServlet.service(request, response);

                    // when
                    verify(response).sendRedirect(path);
                    verify(request, never()).getRequestDispatcher(anyString());
                }
            }

            @Nested
            @DisplayName("POST /login")
            class ServicePostLogin {
                private HttpSession setUpLoginRequest() {
                    when(request.getRequestURI()).thenReturn("/login");
                    when(request.getMethod()).thenReturn("POST");
                    HttpSession session = mock(HttpSession.class);
                    when(request.getSession()).thenReturn(session);

                    return session;
                }

                @Test
                @DisplayName("로그인 된 경우 -> /index.jsp")
                void service_success_post_login_success() throws IOException {
                    // given
                    String path = "/index.jsp";
                    HttpSession session = setUpLoginRequest();
                    var user = new User(1, "gugu", "password", "hkkang@woowahan.com");
                    when(session.getAttribute("user")).thenReturn(user);

                    // then
                    dispatcherServlet.service(request, response);

                    // when
                    verify(response).sendRedirect(path);
                    verify(request, never()).getRequestDispatcher(anyString());
                }

                @Test
                @DisplayName("로그인 안 된 경우, 로그인 성공 -> /index.jsp")
                void service_success_post_register() throws IOException {
                    String path = "/index.jsp";
                    setUpLoginRequest();
                    when(request.getParameter("account")).thenReturn("gugu");
                    when(request.getParameter("password")).thenReturn("password");

                    dispatcherServlet.service(request, response);

                    verify(response).sendRedirect(path);
                    verify(request, never()).getRequestDispatcher(anyString());
                }

                @Nested
                @DisplayName("POST /login 로그인 실패 -> /401.jsp")
                class LoginFail {

                    private static final String REDIRECT_401_JSP = "/401.jsp";

                    @Test
                    @DisplayName("아이디 틀림")
                    void service_success_post_login_fail_account() throws IOException {
                        setUpLoginRequest();
                        when(request.getParameter("account")).thenReturn("kyum");
                        when(request.getParameter("password")).thenReturn("password");

                        dispatcherServlet.service(request, response);

                        verify(response).sendRedirect(REDIRECT_401_JSP);
                        verify(request, never()).getRequestDispatcher(anyString());
                    }

                    @Test
                    @DisplayName("비밀번호 틀림")
                    void service_success_post_login_fail_password() throws IOException {
                        setUpLoginRequest();
                        when(request.getParameter("account")).thenReturn("gugu");
                        when(request.getParameter("password")).thenReturn("password1");

                        dispatcherServlet.service(request, response);

                        verify(response).sendRedirect(REDIRECT_401_JSP);
                        verify(request, never()).getRequestDispatcher(anyString());
                    }
                }

                @Nested
                @DisplayName("POST /login 로그인에 필요한 파라미터가 없을 경우 -> /400.jsp")
                class ServiceLoginNotContainParams {

                    private static final String REDIRECT_400_JSP = "/400.jsp";

                    @Test
                    @DisplayName("아이디가 없는 경우")
                    void not_contain_account_400() throws IOException {
                        when(request.getParameter("password")).thenReturn("new password");
                        setUpLoginRequest();

                        dispatcherServlet.service(request, response);

                        verify(response).sendRedirect(REDIRECT_400_JSP);
                        verify(request, never()).getRequestDispatcher(anyString());
                    }

                    @Test
                    @DisplayName("비밀번호가 없는 경우")
                    void not_contain_password_400() throws IOException {
                        when(request.getParameter("account")).thenReturn("new account");
                        setUpLoginRequest();

                        dispatcherServlet.service(request, response);

                        verify(response).sendRedirect(REDIRECT_400_JSP);
                        verify(request, never()).getRequestDispatcher(anyString());
                    }
                }
            }
        }
    }
}
