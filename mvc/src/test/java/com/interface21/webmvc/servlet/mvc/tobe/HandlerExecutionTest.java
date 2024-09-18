package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class HandlerExecutionTest {

    @DisplayName("@RequestMapping 애너테이션이 붙은 메서드로 요청을 처리한다.")
    @Test
    void handle()
            throws Exception {
        Reflections reflections = new Reflections("samples");

        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        Class<?> controller = controllers.stream()
                .findAny()
                .orElseThrow();

        Method[] methods = controller.getDeclaredMethods();

        Method method = Arrays.stream(methods)
                .filter(m -> {
                    RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
                    return requestMapping != null && Arrays.stream(requestMapping.method())
                            .anyMatch(requestMethod -> requestMethod == RequestMethod.GET);
                })
                .findAny()
                .orElseThrow();

        HandlerExecution handlerExecution = new HandlerExecution(method);

        MockHttpServletRequest request = mock(MockHttpServletRequest.class);
        MockHttpServletResponse response = new MockHttpServletResponse();

        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");

        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView).isNotNull();
        assertThat((String) modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }
}
