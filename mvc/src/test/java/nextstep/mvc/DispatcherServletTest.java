package nextstep.mvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import nextstep.mvc.controller.asis.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;

class DispatcherServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("어노테이션 기반 컨트롤러에 해당하는 요청에 응답할 수 있다.")
    void handleAnnotatedControllerRequest() throws ServletException {
        // given
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");
        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestDispatcher("")).willReturn(mock(RequestDispatcher.class));
        dispatcherServlet = new DispatcherServlet(
                List.of(new AnnotationHandlerMapping("samples")),
                List.of(new AnnotationHandlerAdapter())
        );

        // when
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        // then
        verify(request).getAttribute("id");
    }

    @Test
    @DisplayName("매뉴얼 기반 컨트롤러에 해당하는 요청에 응답할 수 있다.")
    void handleManualControllerRequest() throws ServletException {
        // given
        given(request.getRequestURI()).willReturn("/manual-test");
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        given(request.getRequestDispatcher("testView")).willReturn(requestDispatcher);
        dispatcherServlet = new DispatcherServlet(
                List.of(new TestManualHandlerMapping()), List.of(new ManualHandlerAdapter())
        );

        // when
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        // then
        verify(request).getRequestDispatcher("testView");
    }
}
