package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();
    }

    @Test
    void get_test로_요청을_보낸다() throws ServletException, IOException {
        // given
        final var requestDispatcher = mock(RequestDispatcher.class);

        given(request.getMethod())
                .willReturn(RequestMethod.GET.name());
        given(request.getRequestURI())
                .willReturn("/get-test");
        given(request.getAttribute("id"))
                .willReturn("test");
        given(request.getRequestDispatcher(any()))
                .willReturn(requestDispatcher);

        // when
        dispatcherServlet.service(request, response);

        // then
        final var actual = request.getAttribute("id");
        // TestController 에서 넣어준 Model 값과 viewName 이 유효한지 검증한다.
        assertAll(
                () -> assertThat(actual.toString()).isEqualTo("test"),
                () -> verify(request).getRequestDispatcher("")
        );
    }

    @Test
    void 요청을_처리할_수_없으면_예외가_발생한다() {
        // given
        given(request.getMethod())
                .willReturn(RequestMethod.GET.name());
        given(request.getRequestURI())
                .willReturn("/invalid-path");

        // when, then
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class);
    }
}
