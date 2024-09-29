package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

class ManualHandlerMappingTest {

    @Test
    void suppport() {
        // given
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/login");

        // when
        boolean actual = manualHandlerMapping.support(request);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void getHandler() {
        // given
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        HttpServletRequest request = new MockHttpServletRequest("GET", "/login");

        // when
        Object handler = manualHandlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(Controller.class);
    }
}
