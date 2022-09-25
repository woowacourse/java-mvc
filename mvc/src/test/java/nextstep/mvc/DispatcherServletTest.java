package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void 처리할_수_있는_핸들러가_없으면_ServletException을_반환한다() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        assertThatThrownBy(() -> dispatcherServlet.service(request, response)).isInstanceOf(ServletException.class)
                .hasMessage("처리 가능한 Handler를 찾을 수 없습니다.");
    }

    @Test
    void 처리할_수_없는_HandlerAdapter가_없으면_ServletException을_반환한다() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.init();

        assertThatThrownBy(() -> dispatcherServlet.service(request, response)).isInstanceOf(ServletException.class)
                .hasMessage("처리 가능한 HandlerAdapter를 찾을 수 없습니다.");
    }
}
