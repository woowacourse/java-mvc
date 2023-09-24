package webmvc.org.springframework.web.servlet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DispatcherServletTest {

    @Test
    void handlerMapping을_추가한다() {
        // given
        final HandlerMappings handlerMappings = mock(HandlerMappings.class);
        final HandlerAdapters handlerAdapters = mock(HandlerAdapters.class);
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters);

        // when
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping());

        // then
        verify(handlerMappings, atLeastOnce()).add(any(HandlerMapping.class));
    }

    @Test
    void handlerAdapter를_추가한다() {
        // given
        final HandlerMappings handlerMappings = mock(HandlerMappings.class);
        final HandlerAdapters handlerAdapters = mock(HandlerAdapters.class);
        final DispatcherServlet dispatcherServlet = new DispatcherServlet(handlerMappings, handlerAdapters);

        // when
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        // then
        verify(handlerAdapters, atLeastOnce()).add(any(HandlerAdapter.class));
    }

    @Test
    void 적절한_핸들러_매핑과_어댑터를_찾아_핸들러를_실행한다() throws ServletException, IOException {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        given(request.getRequestURI()).willReturn("/test/get");
        given(request.getMethod()).willReturn("GET");
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}
