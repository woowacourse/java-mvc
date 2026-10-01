package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.RequestMappingHandlerAdapter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.mockito.ArgumentMatchers.anyString;
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
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new RequestMappingHandlerAdapter());

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void 레거시_컨트롤러가_반환한_뷰로_포워드한다() throws Exception {
        final Controller controller = (req, res) -> "/login.jsp";
        dispatcherServlet.addHandlerMapping(anyRequest -> controller);

        dispatcherServlet.service(request, response);

        verify(request).getRequestDispatcher("/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 레거시_컨트롤러가_redirect를_반환하면_리다이렉트한다() throws Exception {
        final Controller controller = (req, res) -> "redirect:/index.jsp";
        dispatcherServlet.addHandlerMapping(anyRequest -> controller);

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
    void 앞의_매핑이_처리하지_못하면_다음_매핑의_핸들러를_실행한다() throws Exception {
        final Controller controller = (req, res) -> "/login.jsp";
        dispatcherServlet.addHandlerMapping(anyRequest -> null);
        dispatcherServlet.addHandlerMapping(anyRequest -> controller);

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
}
