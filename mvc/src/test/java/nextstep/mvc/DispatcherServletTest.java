package nextstep.mvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.RequestMappingHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import samples.TestController;

class DispatcherServletTest {

    private DispatcherServlet dispatcherServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        dispatcherServlet = new DispatcherServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void dispatcherServlet에서_init시_handlerMapping에서_initialize를_실행한다() {
        AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        dispatcherServlet.addHandlerMapping(annotationHandlerMapping);

        dispatcherServlet.init();
        verify(annotationHandlerMapping, times(1)).initialize();
    }

    @Test
    void dispatcherServlet에서_service시_handler와_Adapter를_찾는다() throws Exception {
        AnnotationHandlerMapping annotationHandlerMapping = mock(AnnotationHandlerMapping.class);
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = mock(RequestMappingHandlerAdapter.class);
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(),
                TestController.class.getDeclaredMethods()[0]);
        dispatcherServlet.addHandlerMapping(annotationHandlerMapping);
        dispatcherServlet.addHandlerAdapter(requestMappingHandlerAdapter);
        dispatcherServlet.init();

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");
        when(annotationHandlerMapping.getHandler(request)).thenReturn(handlerExecution);
        when(requestMappingHandlerAdapter.supports(handlerExecution)).thenReturn(true);
        when(requestMappingHandlerAdapter.handle(request, response, handlerExecution))
                .thenReturn(new ModelAndView(new JspView("redirect:/index.jsp")));

        dispatcherServlet.service(request, response);

        verify(annotationHandlerMapping, times(1)).getHandler(request);
        verify(requestMappingHandlerAdapter, times(1)).supports(handlerExecution);
    }
}
