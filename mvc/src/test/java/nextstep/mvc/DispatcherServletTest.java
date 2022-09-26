package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.ManualHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import samples.TestManualHandlerMapping;

@DisplayName("DispatcherService 클래스의")
class DispatcherServletTest {

    private static DispatcherServlet dispatcherServlet;

    @BeforeAll
    private static void setUp() {
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new TestManualHandlerMapping());
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));

        dispatcherServlet.addHandlerAdapter(new ManualHandlerAdapter());
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.init();
    }

    @Nested
    @DisplayName("service 메서드는")
    class Service {

        @Test
        @DisplayName("manual/test 요청을 ManualHandler로 처리한다.")
        void success_manual() {
            //given
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);

            given(request.getRequestURI())
                    .willReturn("/manual/test");

            //when & then
            assertDoesNotThrow(
                    () -> dispatcherServlet.service(request, response)
            );
        }

        @Test
        @DisplayName("/get-test 요청을 AnnotationHandler로 처리한다.")
        void success_annotation() {
            //given
            final var request = mock(HttpServletRequest.class);
            final var response = mock(HttpServletResponse.class);
            final var requestDispatcher = mock(RequestDispatcher.class);

            given(request.getRequestURI())
                    .willReturn("/get-test");
            given(request.getMethod())
                    .willReturn("GET");
            given(request.getAttribute("id"))
                    .willReturn("test");
            given(request.getRequestDispatcher(""))
                    .willReturn(requestDispatcher);

            //when & then
            assertDoesNotThrow(
                    () -> dispatcherServlet.service(request, response)
            );
        }
    }
}
