package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.ControllerHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;
import support.FakeRequestDispatcher;

class DispatcherServletTest {

    @Test
    @DisplayName("어노테이션 기반의 컨트롤러에 대한 요청을 처리할 수 있다.")
    void serviceAnnotationController() throws ServletException {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        final AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        dispatcherServlet.addHandlerMapping(handlerMapping);
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final FakeRequestDispatcher fakeRequestDispatcher = new FakeRequestDispatcher();
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(fakeRequestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(fakeRequestDispatcher.isInvokeForward()).isTrue();
    }

    @Test
    @DisplayName("매뉴얼 기반의 컨트롤러에 대한 요청을 처리할 수 있다.")
    void serviceManualController() throws ServletException {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        final TestManualHandlerMapping manualHandlerMapping = new TestManualHandlerMapping();
        dispatcherServlet.addHandlerMapping(manualHandlerMapping);
        dispatcherServlet.addHandlerAdapter(new ControllerHandlerAdapter());
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final FakeRequestDispatcher fakeRequestDispatcher = new FakeRequestDispatcher();

        when(request.getRequestURI()).thenReturn("/login-test");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(fakeRequestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(fakeRequestDispatcher.isInvokeForward()).isTrue();
    }
}
