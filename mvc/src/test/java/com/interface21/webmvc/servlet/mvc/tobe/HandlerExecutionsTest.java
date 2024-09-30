package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import samples.general.TestController;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerExecutionsTest {

    private final HandlerExecutions handlerExecutions = new HandlerExecutions();

    @DisplayName("uri와 method의 정보를 key로 특정 클래스와 메서드를 매핑한다.")
    @Test
    void mappingHandler() {
        final Method method = mock(Method.class);
        final RequestMapping requestMapping = mock(RequestMapping.class);
        final HttpServletRequest request = new MockHttpServletRequest("GET", "/get-test");

        when(requestMapping.value()).thenReturn("/get-test");
        when(requestMapping.method()).thenReturn(new RequestMethod[]{RequestMethod.GET});

        handlerExecutions.mappingHandler(TestController.class, method, requestMapping);

        assertThat(handlerExecutions.get(request)).isNotNull();
    }
}
