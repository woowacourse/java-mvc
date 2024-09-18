package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionsTest {

    private final HandlerExecutions handlerExecutions = new HandlerExecutions();

    @Test
    void mappingHandler() {
        final Method method = mock(Method.class);
        final RequestMapping requestMapping = mock(RequestMapping.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);

        when(requestMapping.value()).thenReturn("/get-test");
        when(requestMapping.method()).thenReturn(new RequestMethod[]{RequestMethod.GET});
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        handlerExecutions.mappingHandler(TestController.class, method, requestMapping);

        assertThat(handlerExecutions.get(request)).isNotNull();
    }
}
