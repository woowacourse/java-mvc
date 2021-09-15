package nextstep.mvc.mapping;

import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import com.techcourse.air.mvc.core.controller.tobe.HandlerExecution;
import com.techcourse.air.mvc.core.mapping.AnnotationHandlerMapping;
import com.techcourse.air.mvc.core.returnvalue.JsonReturnValueHandler;
import com.techcourse.air.mvc.core.returnvalue.ViewReturnValueHandler;
import com.techcourse.air.mvc.core.view.ModelAndView;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new ApplicationContext("samples");
        ApplicationContextProvider.setApplicationContext(context);
        context.initializeContext();
        context.getBean(ViewReturnValueHandler.class);
        context.getBean(JsonReturnValueHandler.class);
        handlerMapping = context.findBeanByType(AnnotationHandlerMapping.class);
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
