package com.techcourse.handlermapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.techcourse.controller.LoginController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CombinedHandlerMappingTest {

    @DisplayName("RequestMapping Annotation 으로 등록된 uri 요청은 HandlerExecution 타입을 반환한다.")
    @Test
    void getHandler1() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/register");
        given(request.getMethod()).willReturn("GET");

        CombinedHandlerMapping combinedHandlerMapping = new CombinedHandlerMapping("com.techcourse");
        combinedHandlerMapping.initialize();
        Controller controller = combinedHandlerMapping.getHandler(request);

        assertThat(controller).isNotNull();
        assertThat(controller instanceof HandlerExecution).isTrue();
    }

    @DisplayName("RequestMapping Annotation 으로 등록되지 않은 uri 요청은 Controller 타입을 반환한다.")
    @Test
    void getHandler2() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/login");
        given(request.getMethod()).willReturn("GET");

        CombinedHandlerMapping combinedHandlerMapping = new CombinedHandlerMapping("com.techcourse");
        combinedHandlerMapping.initialize();
        Controller controller = combinedHandlerMapping.getHandler(request);

        assertThat(controller).isNotNull();
        assertThat(controller instanceof LoginController).isTrue();
    }
}
