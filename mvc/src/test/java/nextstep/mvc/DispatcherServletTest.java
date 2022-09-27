package nextstep.mvc;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.HandlerExecutionHandlerAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class DispatcherServletTest {

    @Test
    @DisplayName("init호출시 모든 핸들러 매핑의 initialize 메서드가 호출된다.")
    void init() {
        final HandlerMapping handlerMapping1 = mock(HandlerMapping.class);
        final HandlerMapping handlerMapping2 = mock(HandlerMapping.class);
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping1);
        dispatcherServlet.addHandlerMapping(handlerMapping2);

        dispatcherServlet.init();

        verify(handlerMapping1).initialize();
        verify(handlerMapping2).initialize();
    }

    @Test
    @DisplayName("요청이 들어오면 핸들러를 찾는다.")
    void service() throws ServletException, NoSuchMethodException {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        final AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        final TestController testController = new TestController();
        when(annotationHandlerMapping.getHandler(request))
                .thenReturn(
                        new HandlerExecution(
                                testController,
                                testController.getClass()
                                        .getMethod(
                                                "findUserId",
                                                HttpServletRequest.class,
                                                HttpServletResponse.class
                                        )
                        )
                );
        dispatcherServlet.addHandlerMapping(annotationHandlerMapping);
        dispatcherServlet.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        verify(annotationHandlerMapping).getHandler(request);
    }
}
