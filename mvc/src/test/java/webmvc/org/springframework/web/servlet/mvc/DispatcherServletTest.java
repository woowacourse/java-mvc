package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

class DispatcherServletTest {

    private HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    private HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
    private RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
    private HandlerMappings handlerMappings = mock(HandlerMappings.class);
    private HandlerAdapters handlerAdapters = mock(HandlerAdapters.class);
    private HandlerAdapter handlerAdapter = mock(HandlerAdapter.class);
    private HandlerExecution handler = mock(HandlerExecution.class);
    private ModelAndView modelAndView = mock(ModelAndView.class);
    private View view = mock(View.class);

    private DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters);

    @Test
    void HandlerMappings_가_예외를_던지면_예외를_잡고_예외_페이지를_전달한다() throws Exception {
        // given
        given(handlerMappings.getHandler(httpServletRequest))
                .willThrow(IllegalArgumentException.class);
        given(httpServletRequest.getRequestDispatcher("404.jsp"))
                .willReturn(requestDispatcher);
        willDoNothing()
                .given(requestDispatcher);

        // when
        dispatcherServlet.service(httpServletRequest, httpServletResponse);

        // then
        verify(handlerAdapters, times(0))
                .getHandlerAdapter(handler);
        verify(httpServletRequest, times(1))
                .getRequestDispatcher("404.jsp");
    }

    @Test
    void HandlerMappings_예외를_던지지_않으면_정상적으로_진행된다() throws Exception {
        // given
        given(handlerMappings.getHandler(httpServletRequest))
                .willReturn(handler);
        given(handlerAdapters.getHandlerAdapter(handler))
                .willReturn(handlerAdapter);
        given(httpServletRequest.getRequestDispatcher("404.jsp"))
                .willReturn(requestDispatcher);
        given(handlerAdapter.handle(httpServletRequest, httpServletResponse, handler))
                .willReturn(modelAndView);
        given(modelAndView.getView())
                .willReturn(view);
        given(modelAndView.getModel())
                .willReturn(Collections.emptyMap());
        willDoNothing()
                .given(view)
                .render(Collections.emptyMap(), httpServletRequest, httpServletResponse);

        // when
        dispatcherServlet.service(httpServletRequest, httpServletResponse);

        // then
        verify(handlerMappings, times(1))
                .getHandler(httpServletRequest);
        verify(handlerAdapters, times(1))
                .getHandlerAdapter(handler);
        verify(httpServletRequest, times(0))
                .getRequestDispatcher("404.jsp");
    }

    @Test
    void HandlerAdapters_가_예외를_던지면_예외를_잡고_예외_페이지를_전달한다() throws Exception {
        // given
        given(handlerMappings.getHandler(httpServletRequest))
                .willReturn(handler);
        given(handlerAdapters.getHandlerAdapter(handler))
                .willThrow(IllegalArgumentException.class);
        given(httpServletRequest.getRequestDispatcher("404.jsp"))
                .willReturn(requestDispatcher);
        willDoNothing()
                .given(requestDispatcher);

        // when
        dispatcherServlet.service(httpServletRequest, httpServletResponse);

        // then
        verify(modelAndView, times(0))
                .getView();
        verify(httpServletRequest, times(1))
                .getRequestDispatcher("404.jsp");
    }

    @Test
    void HandleAdapters_가_예외를_던지지_않으면_정상적으로_진행된다() throws Exception {
        // given
        given(handlerMappings.getHandler(httpServletRequest))
                .willReturn(handler);
        given(handlerAdapters.getHandlerAdapter(handler))
                .willReturn(handlerAdapter);
        given(httpServletRequest.getRequestDispatcher("404.jsp"))
                .willReturn(requestDispatcher);
        given(handlerAdapter.handle(httpServletRequest, httpServletResponse, handler))
                .willReturn(modelAndView);
        given(modelAndView.getView())
                .willReturn(view);
        given(modelAndView.getModel())
                .willReturn(Collections.emptyMap());
        willDoNothing()
                .given(view)
                .render(Collections.emptyMap(), httpServletRequest, httpServletResponse);

        // when
        dispatcherServlet.service(httpServletRequest, httpServletResponse);

        // then
        verify(handlerMappings, times(1))
                .getHandler(httpServletRequest);
        verify(handlerAdapters, times(1))
                .getHandlerAdapter(handler);
        verify(handlerAdapter, times(1))
                .handle(httpServletRequest, httpServletResponse, handler);
        verify(httpServletRequest, times(0))
                .getRequestDispatcher("404.jsp");
    }
}
