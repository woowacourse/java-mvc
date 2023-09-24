//package webmvc.org.springframework.web.servlet.mvc.tobe;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//
//import java.io.IOException;
//
//@SuppressWarnings("NonAsciiCharacters")
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//class DispatcherServletTest {
//
//    private DispatcherServlet dispatcherServlet;
//
//    @BeforeEach
//    void setup() {
//        dispatcherServlet = new DispatcherServlet();
//        dispatcherServlet.init();
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"/", "/login/view", "/register/view"})
//    void GET_요청을_처리할_수_있다(String requestURI) throws ServletException, IOException {
//        // given
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
//        HttpSession httpSession = mock(HttpSession.class);
//
//        when(request.getRequestURI()).thenReturn(requestURI);
//        when(request.getMethod()).thenReturn("GET");
//        when(request.getSession()).thenReturn(httpSession);
//        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
//
//        when(httpSession.getAttribute(any())).thenReturn(null);
//
//        doNothing().when(requestDispatcher).forward(any(), any());
//
//        // when
//        dispatcherServlet.service(request, response);
//
//        // then
//        verify(requestDispatcher, times(1))
//                .forward(any(), any());
//    }
//
//    @ParameterizedTest
//    @ValueSource(strings = {"/login", "/logout", "/register"})
//    void POST_요청을_처리할_수_있다(String requestURI) throws ServletException, IOException {
//        // given
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
//        HttpSession httpSession = mock(HttpSession.class);
//        User user = new User(1L, "account", "password", "email");
//
//        when(request.getRequestURI()).thenReturn(requestURI);
//        when(request.getMethod()).thenReturn("POST");
//        when(request.getSession()).thenReturn(httpSession);
//        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
//
//        when(request.getParameter("account")).thenReturn("teo");
//        when(request.getParameter("password")).thenReturn("teo");
//        when(request.getParameter("email")).thenReturn("teo");
//
//        when(httpSession.getAttribute(any())).thenReturn(user);
//
//        doNothing().when(requestDispatcher).forward(any(), any());
//
//        // when
//        dispatcherServlet.service(request, response);
//
//        // then
//        verify(response, times(1))
//                .sendRedirect(any());
//    }
//}
