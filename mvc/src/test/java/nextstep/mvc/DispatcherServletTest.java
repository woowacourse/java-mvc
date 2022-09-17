package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.ForwardController;
import nextstep.mvc.controller.tobe.HandlerExecutionAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.asis.HandlerControllerAdapter;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @DisplayName("init 메서드를 호출하면 등록된 handlerMapping 객체들의 initialize 메서드를 한번씩 호출한다.")
    @Test
    void init() {
        final var handlerMapping1 = mock(HandlerMapping.class);
        final var handlerMapping2 = mock(HandlerMapping.class);

        final var dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(handlerMapping1);
        dispatcherServlet.addHandlerMapping(handlerMapping2);
        dispatcherServlet.init();

        verify(handlerMapping1, times(1)).initialize();
        verify(handlerMapping2, times(1)).initialize();
    }

    @DisplayName("service 메서드를 호출하면 handlerMapping을 통해 핸들러를 찾고, 이를 지원하는 handlerAdapter를 통해 핸들러를 실행시킨다.")
    @Nested
    class ServiceTest {

        private DispatcherServlet dispatcherServlet;
        private HttpServletRequest request;
        private HttpServletResponse response;

        @BeforeEach
        void setUp() {
            request = mock(HttpServletRequest.class);
            response = mock(HttpServletResponse.class);

            dispatcherServlet = new DispatcherServlet();
        }

        @DisplayName("HandlerExecution 타입의 핸들러를 찾은 경우 HandlerExecutionAdapter를 통해 실행시킨다.")
        @Test
        void handlerExecution_handler() throws Exception {
            final var handlerMapping = mock(AnnotationHandlerMapping.class);
            final var handlerAdapter = mock(HandlerExecutionAdapter.class);
            final var handlerExecution = new HandlerExecution(null, null);
            dispatcherServlet.addHandlerMapping(handlerMapping);
            dispatcherServlet.addHandlerAdapter(handlerAdapter);
            dispatcherServlet.init();

            when(handlerMapping.getHandler(request)).thenReturn(handlerExecution);
            when(handlerAdapter.supports(handlerExecution)).thenReturn(true);
            when(handlerAdapter.handle(request, response, handlerExecution))
                    .thenReturn(new ModelAndView(new JspView("index.jsp")));

            dispatcherServlet.service(request, response);

            verify(handlerMapping, times(1)).getHandler(request);
            verify(handlerAdapter, times(1)).supports(handlerExecution);
            verify(handlerAdapter, times(1)).handle(request, response, handlerExecution);
        }

        @DisplayName("Controller 타입의 핸들러를 찾은 경우 HandlerControllerAdapter를 통해 실행시킨다.")
        @Test
        void controller_handler() throws Exception {
            final var handlerMapping = mock(HandlerMapping.class);
            final var handlerAdapter = mock(HandlerControllerAdapter.class);
            final var controller = new ForwardController("/");
            dispatcherServlet.addHandlerMapping(handlerMapping);
            dispatcherServlet.addHandlerAdapter(handlerAdapter);
            dispatcherServlet.init();

            when(handlerMapping.getHandler(request)).thenReturn(controller);
            when(handlerAdapter.supports(controller)).thenReturn(true);
            when(handlerAdapter.handle(request, response, controller))
                    .thenReturn(new ModelAndView(new JspView("index.jsp")));

            dispatcherServlet.service(request, response);

            verify(handlerMapping, times(1)).getHandler(request);
            verify(handlerAdapter, times(1)).supports(controller);
            verify(handlerAdapter, times(1)).handle(request, response, controller);
        }
    }
}
