package nextstep.mvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        dispatcherServlet = new DispatcherServlet();
    }

    @Test
    @DisplayName("어노테이션 기반 컨트롤러에 해당하는 요청에 응답할 수 있다.")
    void handleAnnotatedControllerRequest() throws ServletException, IOException {
        // given
            given(request.getRequestURI()).willReturn("/get-test");
            given(request.getMethod()).willReturn("GET");
            given(request.getAttribute("id")).willReturn("gugu");

        // when
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        // then
        verify(response).sendRedirect("");
    }
}
