package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Nested
    @DisplayName("service 메소드는")
    class Service {

        @Test
        @DisplayName("Service 메소드는 요청에 매핑되는 핸들러와 핸들러 어댑터를 찾고, 핸들러를 실행한다.")
        void success() throws Exception {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher("")).thenReturn(requestDispatcher);

            final DispatcherServlet dispatcherServlet = new DispatcherServlet();
            dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
            dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());
            dispatcherServlet.init();

            // when
            dispatcherServlet.service(request, response);

            // then
            verify(requestDispatcher, times(1)).forward(request, response);
        }

        @Test
        @DisplayName("Service 메소드는 요청에 매핑되는 핸들러가 없다면 response의 Status를 404로 설정하고 메소드 실행을 종료한다.")
        void failService_notFoundHandler() throws Exception {
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            when(request.getRequestURI()).thenReturn("/test");
            when(request.getMethod()).thenReturn("GET");

            final DispatcherServlet dispatcherServlet = new DispatcherServlet();

            // when
            dispatcherServlet.service(request, response);

            // then
            verify(response, times(1)).setStatus(404);
        }
    }
}