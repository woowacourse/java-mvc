package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.controller.tobe.adapter.ModelAndViewHandlerAdapter;
import nextstep.mvc.controller.tobe.adapter.ViewNameHandlerAdapter;
import nextstep.mvc.controller.tobe.adapter.VoidHandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.NullView;
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

        @DisplayName("HandlerExecution 타입이면서 ModelAndView를 반환하는 핸들러를 찾은 경우 ModelAndViewHandlerAdapter를 통해 실행시킨다.")
        @Test
        void handlerExecution_modelAndViewHandlerAdapter() throws Exception {
            final var handlerMapping = mock(AnnotationHandlerMapping.class);
            final var handlerAdapter = mock(ModelAndViewHandlerAdapter.class);
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

        @DisplayName("HandlerExecution 타입이면서 String을 반환하는 핸들러를 찾은 경우 ViewNameHandlerAdapter를 통해 실행시킨다.")
        @Test
        void handlerExecution_viewNameHandlerAdapter() throws Exception {
            final var handlerMapping = mock(AnnotationHandlerMapping.class);
            final var handlerAdapter = mock(ViewNameHandlerAdapter.class);
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

        @DisplayName("HandlerExecution 타입이면서 반환값이 없는 핸들러를 찾은 경우 VoidHandlerAdapter를 통해 실행시킨다.")
        @Test
        void handlerExecution_voidHandlerAdapter() throws Exception {
            final var handlerMapping = mock(AnnotationHandlerMapping.class);
            final var handlerAdapter = mock(VoidHandlerAdapter.class);
            final var handlerExecution = new HandlerExecution(null, null);
            dispatcherServlet.addHandlerMapping(handlerMapping);
            dispatcherServlet.addHandlerAdapter(handlerAdapter);
            dispatcherServlet.init();

            when(handlerMapping.getHandler(request)).thenReturn(handlerExecution);
            when(handlerAdapter.supports(handlerExecution)).thenReturn(true);
            when(handlerAdapter.handle(request, response, handlerExecution))
                    .thenReturn(new ModelAndView(new NullView()));

            dispatcherServlet.service(request, response);

            verify(handlerMapping, times(1)).getHandler(request);
            verify(handlerAdapter, times(1)).supports(handlerExecution);
            verify(handlerAdapter, times(1)).handle(request, response, handlerExecution);
        }
    }
}
