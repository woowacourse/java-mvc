package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ControllerHandlerAdapterTest {

    private HandlerAdapter handlerAdapter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerAdapter = new ControllerHandlerAdapter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void Controller_인터페이스의_구현체를_지원한다() {
        final Controller controller = new ForwardController("/index.jsp");

        assertThat(handlerAdapter.supports(controller)).isTrue();
    }

    @Test
    void Controller_인터페이스의_구현체가_아니면_지원하지_않는다() {
        final var handlerExecution = mock(HandlerExecution.class);

        assertThat(handlerAdapter.supports(handlerExecution)).isFalse();
    }

    @Test
    void 컨트롤러가_반환한_뷰_이름을_JspView를_가진_ModelAndView로_변환한다() throws Exception {
        final Controller controller = (req, res) -> "/index.jsp";
        final var requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcher);

        final var modelAndView = handlerAdapter.handle(request, response, controller);

        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
        modelAndView.getView().render(modelAndView.getModel(), request, response);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void 컨트롤러가_반환한_redirect_뷰_이름도_그대로_동작한다() throws Exception {
        final Controller controller = (req, res) -> "redirect:/index.jsp";

        final var modelAndView = handlerAdapter.handle(request, response, controller);

        modelAndView.getView().render(modelAndView.getModel(), request, response);
        verify(response).sendRedirect("/index.jsp");
    }
}
