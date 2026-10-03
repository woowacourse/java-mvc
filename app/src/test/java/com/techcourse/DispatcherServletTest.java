package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();
    }

    @Test
    void 컨트롤러가_JSP_뷰_이름을_반환하면_포워드한다() throws Exception {
        final var request = createRequest("GET", "/register/view");
        final var response = mock(HttpServletResponse.class);
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 컨트롤러가_redirect_뷰_이름을_반환하면_리다이렉트한다() throws Exception {
        final var request = createRequest("GET", "/logout");
        final var response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void 처리할_핸들러가_없으면_404를_응답한다() throws Exception {
        final var request = createRequest("GET", "/not-found");
        final var response = mock(HttpServletResponse.class);

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 외부에서_등록한_매핑과_어댑터로_요청을_처리한다() throws Exception {
        final var customHandler = new Object();
        dispatcherServlet.addHandlerMapping(request ->
                "/custom".equals(request.getRequestURI()) ? customHandler : null);
        dispatcherServlet.addHandlerAdapter(new HandlerAdapter() {
            @Override
            public boolean supports(Object handler) {
                return handler == customHandler;
            }

            @Override
            public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
                return new ModelAndView(new JspView("redirect:/custom-result"));
            }
        });
        final var request = createRequest("GET", "/custom");
        final var response = mock(HttpServletResponse.class);

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/custom-result");
    }

    private HttpServletRequest createRequest(String method, String requestURI) {
        final var request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn(requestURI);
        return request;
    }
}
