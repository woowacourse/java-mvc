package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    @Test
    void init() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        HandlerMapping manualHandlerMapping = mock(HandlerMapping.class);
        HandlerMapping annotationHandlerMapping = mock(HandlerMapping.class);
        dispatcherServlet.addHandlerMapping(manualHandlerMapping);
        dispatcherServlet.addHandlerMapping(annotationHandlerMapping);

        dispatcherServlet.init();

        verify(manualHandlerMapping).initialize();
        verify(annotationHandlerMapping).initialize();
    }

    @Test
    void service() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(annotationHandlerMapping);
        dispatcherServlet.init();

        HandlerExecution handlerExecution = (HandlerExecution) annotationHandlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(modelAndView.getModel()).hasSize(1);
        assertThatThrownBy(() -> dispatcherServlet.service(request, response))
                .isInstanceOf(ServletException.class)
                .hasMessage("JSP not found.");
    }
}
