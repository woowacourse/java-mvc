package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.View;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    @Test
    void service() throws Exception {
        //given
        HandlerMappings handlerMappings = mock(HandlerMappings.class);
        HandlerExecutor handlerExecutor = mock(HandlerExecutor.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        ModelAndView modelAndView = mock(ModelAndView.class);
        View view = mock(View.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerExecutor);

        when(request.getMethod()).thenReturn("GET");
        when(handlerMappings.getHandler(any())).thenReturn(handlerExecutor);
        when(handlerExecutor.execute(any(), any(), any())).thenReturn(modelAndView);
        when(modelAndView.getView()).thenReturn(view);
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        //when
        dispatcherServlet.service(request, response);

        //then
        verify(handlerExecutor, times(1)).execute(any(), any(), any());
        verify(view, times(1)).render(any(), any(), any());
    }
}
