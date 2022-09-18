package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionAdapter;
import nextstep.mvc.support.MockRequestDispatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet = null;

    @BeforeEach
    void setup() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionAdapter());
        dispatcherServlet.init();
    }

    @Test
    void view가_반환되면_해당_view로_forward한다() throws ServletException {
        // given
        final MockRequestDispatcher requestDispatcher = new MockRequestDispatcher();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getRequestDispatcher("view")).thenReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        assertThat(requestDispatcher.isForwardExecuted()).isTrue();
    }
}
