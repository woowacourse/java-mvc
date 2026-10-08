package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import com.interface21.webmvc.servlet.mvc.exception.AdapterNotFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet("samples");
        dispatcherServlet.addHandlerAdapter(new TestHandlerAdapter());

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void 핸들러가_반환한_뷰로_포워드한다() throws Exception {
        final TestHandler handler = (req, res) -> new ModelAndView(new JspView("/login.jsp"));
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);

        dispatcherServlet.service(request, response);

        verify(request).getRequestDispatcher("/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 핸들러가_redirect_뷰를_반환하면_리다이렉트한다() throws Exception {
        final TestHandler handler = (req, res) -> new ModelAndView(new JspView("redirect:/index.jsp"));
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);

        dispatcherServlet.service(request, response);

        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    void 어노테이션_컨트롤러를_실행하고_모델을_담아_포워드한다() throws Exception {
        final Method method = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(new TestController(), method);
        dispatcherServlet.addHandlerMapping(anyRequest -> handlerExecution);
        when(request.getAttribute("id")).thenReturn("gugu");

        dispatcherServlet.service(request, response);

        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 패키지를_지정하지_않고_생성하면_예외가_발생한다() {
        assertThatThrownBy(DispatcherServlet::new)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성자로_받은_패키지의_어노테이션_컨트롤러를_별도_등록_없이_실행한다() throws Exception {
        final DispatcherServlet servlet = new DispatcherServlet("samples");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getAttribute("id")).thenReturn("gugu");

        servlet.service(request, response);

        verify(request).setAttribute("id", "gugu");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 앞의_매핑이_처리하지_못하면_다음_매핑의_핸들러를_실행한다() throws Exception {
        final TestHandler handler = (req, res) -> new ModelAndView(new JspView("/login.jsp"));
        dispatcherServlet.addHandlerMapping(anyRequest -> null);
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);

        dispatcherServlet.service(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 처리할_핸들러가_없으면_404를_응답한다() throws Exception {
        dispatcherServlet.addHandlerMapping(anyRequest -> null);

        dispatcherServlet.service(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void 처리할_핸들러가_없으면_포워드하지_않는다() throws Exception {
        dispatcherServlet.addHandlerMapping(anyRequest -> null);

        dispatcherServlet.service(request, response);

        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void 핸들러를_처리할_어댑터가_없으면_ServletException으로_감싸_던진다() {
        final Object unsupportedHandler = new Object();
        dispatcherServlet.addHandlerMapping(anyRequest -> unsupportedHandler);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCauseInstanceOf(AdapterNotFoundException.class);
    }

    @Test
    void 핸들러가_IOException을_던지면_감싸지_않고_그대로_던진다() {
        final IOException expected = new IOException("handler failed");
        final TestHandler handler = (req, res) -> {
            throw expected;
        };
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isSameAs(expected);
    }

    @Test
    void 핸들러가_ServletException을_던지면_감싸지_않고_그대로_던진다() {
        final ServletException expected = new ServletException("handler failed");
        final TestHandler handler = (req, res) -> {
            throw expected;
        };
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isSameAs(expected);
    }

    @Test
    void 포워드_중_IOException이_발생하면_감싸지_않고_그대로_던진다() throws Exception {
        final IOException expected = new IOException("forward failed");
        final TestHandler handler = (req, res) -> new ModelAndView(new JspView("/login.jsp"));
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);
        doThrow(expected).when(requestDispatcher).forward(request, response);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isSameAs(expected);
    }

    @Test
    void 핸들러가_그_외_checked_예외를_던지면_ServletException으로_감싸_던진다() {
        final Exception expected = new Exception("handler failed");
        final TestHandler handler = (req, res) -> {
            throw expected;
        };
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);

        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasCause(expected);
    }

    @Test
    void 어댑터가_null_ModelAndView를_반환하면_렌더링하지_않는다() throws Exception {
        final TestHandler handler = (req, res) -> null;
        dispatcherServlet.addHandlerMapping(anyRequest -> handler);

        dispatcherServlet.service(request, response);

        verify(request, never()).getRequestDispatcher(anyString());
        verify(response, never()).sendRedirect(anyString());
    }

    @FunctionalInterface
    private interface TestHandler {
        ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
    }

    private static class TestHandlerAdapter implements HandlerAdapter {

        @Override
        public boolean supports(final Object handler) {
            return handler instanceof TestHandler;
        }

        @Override
        public ModelAndView handle(final HttpServletRequest request,
                                   final HttpServletResponse response,
                                   final Object handler) throws Exception {
            return ((TestHandler) handler).handle(request, response);
        }
    }
}
