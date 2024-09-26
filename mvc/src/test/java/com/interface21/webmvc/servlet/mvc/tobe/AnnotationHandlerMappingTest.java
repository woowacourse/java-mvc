package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private HandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var result = handlerExecution.handle(request, response);
        ModelAndView modelAndView = (ModelAndView) result;

        assertThat(result).isInstanceOf(ModelAndView.class);
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = (ModelAndView) handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("initialize 메서드를 실행하면 basePackage 의 컨트롤러를 전부 읽어서 handlerKey와 handlerExecution을 등록한다.")
    @Test
    void initialize() throws NoSuchFieldException, IllegalAccessException {
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");

        annotationHandlerMapping.initialize();

        Field field = annotationHandlerMapping.getClass().getDeclaredField("handlerExecutions");
        field.setAccessible(true);
        Map<HandlerKey, HandlerExecution> handlerExecutions =
                (Map<HandlerKey, HandlerExecution>) field
                        .get(annotationHandlerMapping);

        assertThat(handlerExecutions).containsKey(new HandlerKey("/get-test", RequestMethod.GET));
        assertThat(handlerExecutions).containsKey(new HandlerKey("/post-test", RequestMethod.POST));
        assertThat(handlerExecutions.get(new HandlerKey("/get-test", RequestMethod.GET))).isNotNull();
        assertThat(handlerExecutions.get(new HandlerKey("/post-test", RequestMethod.POST))).isNotNull();
    }
}
