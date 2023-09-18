package com.techcourse.support.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.controller.LoginViewController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMapping;

class ManualHandlerMappingTest {

    private final HandlerMapping handlerMapping = new ManualHandlerMapping();

    @BeforeEach
    void setup(){
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("요청에 대한 인터페이스 기반 핸들러를 반환할 수 있다.")
    void getHandler() {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/login/view");
        when(request.getMethod()).thenReturn("GET");

        //when
        Object controller = handlerMapping.getHandler(request);

        //then
        assertThat(controller).isInstanceOf(LoginViewController.class);
    }

}